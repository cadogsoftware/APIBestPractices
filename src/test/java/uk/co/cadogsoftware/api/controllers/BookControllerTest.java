package uk.co.cadogsoftware.api.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import uk.co.cadogsoftware.api.assemblers.BookModelAssembler;
import uk.co.cadogsoftware.api.dtos.BookDTO;
import uk.co.cadogsoftware.api.exceptions.BookNotFoundException;
import uk.co.cadogsoftware.api.services.BookService;

/**
 * Test class for {@link BookController}.
 */
@WebMvcTest({BookController.class, BookModelAssembler.class})
class BookControllerTest {

  private static final String EXPECTED_ALL_BOOKS_RESPONSE = """
      {"_embedded":{"bookDTOList":[{"author":"George Orwell","authorFirstName":"George","authorLastName":"Orwell","isbn":"1","title":"Animal Farm","_links":{"self":{"href":"http://localhost/books/1"},"books":{"href":"http://localhost/books?title="}}},{"author":"George Orwell2","authorFirstName":"George","authorLastName":"Orwell2","isbn":"2","title":"Animal Farm 2","_links":{"self":{"href":"http://localhost/books/2"},"books":{"href":"http://localhost/books?title="}}}]},"_links":{"self":{"href":"http://localhost/books?title="}}}"
      """;

  private static final String EXPECTED_SINGLE_BOOK_RESPONSE = """
      {"author":"George Orwell","authorFirstName":"George","authorLastName":"Orwell","isbn":"1","title":"Animal Farm","_links":{"self":{"href":"http://localhost/books/1"},"books":{"href":"http://localhost/books?title="}}}
      """;

  private static final String EXPECTED_LINKS_SINGLE_BOOK = """
        "_links":{"self":{"href":"http://localhost/books/1"},"books":{"href":"http://localhost/books?title="}}""";

  private static final BookDTO FIRST_BOOK = new BookDTO("George Orwell", "George", "Orwell", "1", "Animal Farm");
  private static final BookDTO SECOND_BOOK = new BookDTO("George Orwell2", "George", "Orwell2","2", "Animal Farm 2");

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private BookService service;

  @Autowired
  private BookModelAssembler bookModelAssembler;

  /**
   * This test shows several ways of checking the response. Of course there is no need to do all of
   * these, but I've left them in here as examples of what is possible.
   */
  @Test
  void getOneBook_ShouldReturnTheCorrectBook() throws Exception {
    String pathToTest = "/books/1";

    when(service.getBook("1")).thenReturn(FIRST_BOOK);

    /*
    There are several ways of testing the response from the controller here.
    We obviously only need to follow one of these patterns,
    but I'm showing them here as examples of what is possible.
    My preferred method is the last one - number 4.
    */

    // 1. Call the Controller and test that the response contains some of what we expect.
    this.mockMvc.perform(get(pathToTest)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("Animal Farm")))
        .andExpect(content().string(containsString(EXPECTED_LINKS_SINGLE_BOOK)));

    // 2. Split it up a bit and test the json in the response.
    // Note that this does not do strict json checking so the json we are expecting is not the
    // complete json, just some of it. Use json(String, true) to enable strict checking.
    String bookOneAsJson = """
        { "author": "George Orwell", "isbn": "1", "title": "Animal Farm"}
        """;
    ResultActions result2 = this.mockMvc.perform(get(pathToTest));
    result2.andExpect(status().isOk())
        .andExpect(content().json(bookOneAsJson))
        .andExpect(content().string(containsString(EXPECTED_LINKS_SINGLE_BOOK)));

    // 3. Test the book returned. This does not test the HATEOAS links.
    ResultActions result3 = this.mockMvc.perform(get(pathToTest));
    result3.andExpect(status().isOk());
    String responseAsString = result3.andReturn().getResponse().getContentAsString();
    BookDTO bookFromResponse = objectMapper.readValue(responseAsString, BookDTO.class);
    assertEquals(FIRST_BOOK, bookFromResponse);

    // 4. My preferred way - test the complete response, including the HATEOAS links.
    // Note the 'strict' checking of the json here.
    String expectedBookDTOAsJson = objectMapper.writeValueAsString(FIRST_BOOK);
    this.mockMvc.perform(get(pathToTest))
        .andExpect(status().isOk())
        // The next line is overkill as we are checking the whole response later,
        // but left in here to show what is possible.
        .andExpect(content().json(expectedBookDTOAsJson))
        // Again, the next line is overkill as we are checking the complete
        // response in the line after but left in here to show what is possible.
        .andExpect(content().string(containsString(EXPECTED_LINKS_SINGLE_BOOK)))
        // Note the strict json checking in the next line.
        .andExpect(content().json(EXPECTED_SINGLE_BOOK_RESPONSE, true));
  }

  @Test
  void getOneBook_NotFound() throws Exception {
    String isbn = "123";
    String pathToTest = "/books/" + isbn;

    when(service.getBook(isbn)).thenThrow(new BookNotFoundException(isbn));

    this.mockMvc.perform(get(pathToTest)).andDo(print()).andExpect(status().isNotFound())
        .andExpect(content().string(containsString(
            "Book cannot be found for ISBN: " + isbn)));
  }

  @Test
  void getBooks_NoTitleFilter() throws Exception {
    String pathToTest = "/books";
    List<BookDTO> bookList = List.of(FIRST_BOOK, SECOND_BOOK);

    when(service.findBooks(null)).thenReturn(bookList);

    this.mockMvc.perform(get(pathToTest))
        .andExpect(status().isOk())
        .andExpect(content().json(EXPECTED_ALL_BOOKS_RESPONSE));
  }

  @Test
  void getBooks_WithTitleFilter_BooksFound() throws Exception {
    String title = "fred";
    String pathToTest = "/books?title=" + title;
    List<BookDTO> bookList = List.of(FIRST_BOOK, SECOND_BOOK);

    when(service.findBooks(title)).thenReturn(bookList);

    this.mockMvc.perform(get(pathToTest))
        .andExpect(status().isOk())
        .andExpect(content().json(EXPECTED_ALL_BOOKS_RESPONSE));
  }

  @Test
  @DisplayName("If no books are found then a successful response code and an empty list should"
      + "be returned.")
  void getBooks_WithTitleFilter_NoBooksFound() throws Exception {
    String title = "fred";
    String pathToTest = "/books?title=" + title;

    when(service.findBooks(title)).thenReturn(List.of());

    this.mockMvc.perform(get(pathToTest))
        .andExpect(status().isNotFound());
  }

  @Test
  void addBook() throws Exception {
    String pathToTest = "/books";
    when(service.addBook(FIRST_BOOK)).thenReturn(FIRST_BOOK);

    String firstBookAsJson = objectMapper.writeValueAsString(FIRST_BOOK);

    this.mockMvc.perform(post(pathToTest).content(firstBookAsJson).contentType("application/hal+json"))
        .andExpect(status().isOk())
        .andExpect(content().json(EXPECTED_SINGLE_BOOK_RESPONSE, true));
  }

  @Test
  void addBook_InvalidBook_NoAuthor() throws Exception {
    String pathToTest = "/books";

    BookDTO invalidBookNoAuthor = new BookDTO("", "", "", "1", "Animal Farm");
    String bookAsJson = objectMapper.writeValueAsString(invalidBookNoAuthor);

    this.mockMvc.perform(post(pathToTest).content(bookAsJson).contentType("application/hal+json"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void addBook_InvalidBook_NoAuthorFirstName() throws Exception {
    String pathToTest = "/books";

    BookDTO invalidBookNoAuthor = new BookDTO("", " ", "last", "1", "Animal Farm");
    String bookAsJson = objectMapper.writeValueAsString(invalidBookNoAuthor);

    this.mockMvc.perform(post(pathToTest).content(bookAsJson).contentType("application/hal+json"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void addBook_InvalidBook_NoAuthorLastName() throws Exception {
    String pathToTest = "/books";

    BookDTO invalidBookNoAuthor = new BookDTO(" ", "first", "", "1", "Animal Farm");
    String bookAsJson = objectMapper.writeValueAsString(invalidBookNoAuthor);

    this.mockMvc.perform(post(pathToTest).content(bookAsJson).contentType("application/hal+json"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @DisplayName("Deleting a book that exists returns ok.")
  void removeBook_Exists() throws Exception {
    String pathToTest = "/books/1";
    String isbn = "1";

    this.mockMvc.perform(delete(pathToTest))
        .andExpect(status().isOk());

    verify(service).removeBook(isbn);
  }

  @Test
  @DisplayName("Deleting a book that does not exist returns ok.")
  void removeBook_DoesNotExist() throws Exception {
    String pathToTest = "/books/199";
    String isbn = "199";

    this.mockMvc.perform(delete(pathToTest))
        .andExpect(status().isOk());

    verify(service).removeBook(isbn);
  }

}
