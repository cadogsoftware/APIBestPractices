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
    BookDTO firstBook = new BookDTO("George Orwell", "1", "Animal Farm");

    when(service.getBook("1")).thenReturn(firstBook);

    /*
    There are several ways of testing the response from the controller here.
    We obviously only need to follow one of these patterns,
    but I'm showing them here as examples of what is possible.
    My preferred method is the last one - number 4.
    */

    String expectedLinks = "\"_links\":{\"self\":{\"href\":\"http://localhost/books/1\"},\"books\":{\"href\":\"http://localhost/books?title=\"}}";

    // 1. Call the Controller and test that the response contains some of what we expect.
    this.mockMvc.perform(get(pathToTest)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("Animal Farm")))
        .andExpect(content().string(containsString(expectedLinks)));

    // 2. Split it up a bit and test the json in the response.
    // Note that this does not do strict json checking so the json we are expecting is not the
    // complete json, just some of it. Use json(String, true) to enable strict checking.
    ResultActions result2 = this.mockMvc.perform(get(pathToTest));
    result2.andExpect(status().isOk())
        .andExpect(content().json(
            "{ \"author\": \"George Orwell\", \"isbn\": \"1\", \"title\": \"Animal Farm\"}"
        )).andExpect(content().string(containsString(expectedLinks)));

    // 3. Test the book is returned in the response.
    ResultActions result3 = this.mockMvc.perform(get(pathToTest));
    result3.andExpect(status().isOk());
    String responseAsString = result3.andReturn().getResponse().getContentAsString();
    BookDTO bookFromResponse = objectMapper.readValue(responseAsString, BookDTO.class);
    assertEquals(firstBook, bookFromResponse);

    // 4. My preferred way - test the complete response, including the HATEOAS links.
    // Note the 'strict' checking of the json here.
    String expectedCompleteResponse = """
        {"author":"George Orwell","isbn":"1","title":"Animal Farm","_links":{"self":{"href":"http://localhost/books/1"},"books":{"href":"http://localhost/books?title="}}}
        """;
    String expectedBookDTOAsJson = objectMapper.writeValueAsString(firstBook);
    this.mockMvc.perform(get(pathToTest))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedBookDTOAsJson)) // overkill as we are checking the whole response but left in here to show what is possible.
        .andExpect(content().string(containsString(expectedLinks))) // again, this is overkill.
        .andExpect(content().json(expectedCompleteResponse, true)); // note the strict checking here.
  }

  @Test
  void getOneBook_NotFound() throws Exception {
    String isbn = "123";
    String pathToTest = "/books/" + isbn;

    when(service.getBook(isbn)).thenThrow(new BookNotFoundException(isbn));

    this.mockMvc.perform(get(pathToTest)).andDo(print()).andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Book cannot be found for ISBN: " + isbn)));
  }

  @Test
  void getBooks_NoTitleFilter() throws Exception {
    String pathToTest = "/books";
    BookDTO firstBook = new BookDTO("George Orwell", "1", "Animal Farm");
    BookDTO secondBook = new BookDTO("George Orwell 2", "2", "Animal Farm 2");
    List<BookDTO> bookList = List.of(firstBook, secondBook);

    when(service.findBooks(null)).thenReturn(bookList);

    String expectedResponse = """
        {"_embedded":{"bookDTOList":[{"author":"George Orwell","isbn":"1","title":"Animal Farm","_links":{"self":{"href":"http://localhost/books/1"},"books":{"href":"http://localhost/books?title="}}},{"author":"George Orwell 2","isbn":"2","title":"Animal Farm 2","_links":{"self":{"href":"http://localhost/books/2"},"books":{"href":"http://localhost/books?title="}}}]},"_links":{"self":{"href":"http://localhost/books?title="}}}"
        """;

    this.mockMvc.perform(get(pathToTest))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  void getBooks_WithTitleFilter_BooksFound() throws Exception {
    String title = "fred";
    String pathToTest = "/books?title=" + title;
    BookDTO firstBook = new BookDTO("George Orwell", "1", "Animal Farm");
    BookDTO secondBook = new BookDTO("George Orwell 2", "2", "Animal Farm 2");
    List<BookDTO> bookList = List.of(firstBook, secondBook);

    when(service.findBooks(title)).thenReturn(bookList);

    String expectedResponse = """
        {"_embedded":{"bookDTOList":[{"author":"George Orwell","isbn":"1","title":"Animal Farm","_links":{"self":{"href":"http://localhost/books/1"},"books":{"href":"http://localhost/books?title="}}},{"author":"George Orwell 2","isbn":"2","title":"Animal Farm 2","_links":{"self":{"href":"http://localhost/books/2"},"books":{"href":"http://localhost/books?title="}}}]},"_links":{"self":{"href":"http://localhost/books?title="}}}"
        """;

    this.mockMvc.perform(get(pathToTest))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponse));
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
    BookDTO firstBook = new BookDTO("George Orwell", "1", "Animal Farm");
    when(service.addBook(firstBook)).thenReturn(firstBook);

    String firstBookAsJson = objectMapper.writeValueAsString(firstBook);

    this.mockMvc.perform(post(pathToTest).content(firstBookAsJson).contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().json(firstBookAsJson));
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
