package uk.co.cadogsoftware.api.testutils;


import lombok.experimental.UtilityClass;
import uk.co.cadogsoftware.api.database.entities.Book;
import uk.co.cadogsoftware.api.dtos.BookDTO;

@UtilityClass
public class TestFixtures {
  public static final String AUTHOR_1_FIRSTNAME = "George";
  public static final String AUTHOR_1_LASTNAME = "Orwell";
  public static final String AUTHOR_1 = AUTHOR_1_FIRSTNAME + " " + AUTHOR_1_LASTNAME;
  public static final String AUTHOR_2_FIRSTNAME = "Tom";
  public static final String AUTHOR_2_LASTNAME = "Smith";
  public static final String AUTHOR_2 = AUTHOR_2_FIRSTNAME + " " + AUTHOR_2_LASTNAME;
  public static final String AUTHOR_3_FIRSTNAME = "The";
  public static final String AUTHOR_3_LASTNAME = "Fonz";
  public static final String AUTHOR_3 = AUTHOR_3_FIRSTNAME + " " + AUTHOR_3_LASTNAME;
  public static final String ISBN_1 = "123";
  public static final String ISBN_2 = "456";
  public static final String ISBN_3 = "789";

  public static final String TITLE_1 = "Animal Farm";
  public static final String TITLE_2 = "Farming World";
  public static final String TITLE_3 = "Happy Days";

  public static final BookDTO TEST_BOOKDTO_1 = new BookDTO(AUTHOR_1, AUTHOR_1_FIRSTNAME, AUTHOR_1_LASTNAME, ISBN_1, TITLE_1);
  public static final BookDTO TEST_BOOKDTO_2 = new BookDTO(AUTHOR_2, AUTHOR_2_FIRSTNAME, AUTHOR_2_LASTNAME, ISBN_2, TITLE_2);
  public static final BookDTO TEST_BOOKDTO_3 = new BookDTO(AUTHOR_3, AUTHOR_3_FIRSTNAME, AUTHOR_3_LASTNAME, ISBN_3, TITLE_3);
  public static final BookDTO TEST_BOOKDTO_JUST_AUTHOR = new BookDTO(AUTHOR_1, "", "", ISBN_1, TITLE_1);
  public static final BookDTO TEST_BOOKDTO_NO_AUTHOR = new BookDTO("", AUTHOR_1_FIRSTNAME, AUTHOR_1_LASTNAME, ISBN_1, TITLE_1);

  public static final Book TEST_BOOK_1 = new Book(AUTHOR_1_FIRSTNAME, AUTHOR_1_LASTNAME, ISBN_1, TITLE_1);
  public static final Book TEST_BOOK_2 = new Book(AUTHOR_2_FIRSTNAME, AUTHOR_2_LASTNAME, ISBN_2, TITLE_2);
}
