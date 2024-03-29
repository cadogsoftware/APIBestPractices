package uk.co.cadogsoftware.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import uk.co.cadogsoftware.api.controllers.BookController;
import uk.co.cadogsoftware.api.dtos.BookDTO;

/**
 * Creates HATOAS links in the BootDTO ready to be sent in the REST response.
 */
@Component
public class BookModelAssembler implements RepresentationModelAssembler<BookDTO, EntityModel<BookDTO>> {

  @Override
  public EntityModel<BookDTO> toModel(BookDTO bookDTO) {

    return EntityModel.of(bookDTO,
        linkTo(methodOn(BookController.class).getOneBook(bookDTO.getIsbn())).withSelfRel(),
        linkTo(methodOn(BookController.class).getBooks("")).withRel("books"));
  }
}

