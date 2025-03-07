package com.custempmanag.librarymanagmentsystem.service;

import com.custempmanag.librarymanagmentsystem.dto.BooksDTO;
import com.custempmanag.librarymanagmentsystem.exception.CustomException;
import com.custempmanag.librarymanagmentsystem.models.Books;
import com.custempmanag.librarymanagmentsystem.repository.BooksRepository;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BooksService {

    @Autowired
    private ModelMapper modelMapper;

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Cacheable(value = "booksCache", key = "'allBooks'")
    public MessageResponse getAllBooks() {
        List<Books> books = booksRepository.findAll();
        if(books.isEmpty())
            return new MessageResponse(HttpStatus.NOT_FOUND.toString(), "No books found!", null);

        List<BooksDTO> bookDTOs = books.stream()
                .map(BooksDTO::new)
                .collect(Collectors.toList());

        return new MessageResponse(HttpStatus.OK.toString(), "Books retrieved successfully!", bookDTOs);
    }

    @Cacheable(value = "booksCache", key = "#id")
    public MessageResponse getBookById(Long id) {
        Books books = booksRepository.findById(id)
                .orElseThrow(() -> new CustomException("Book not found!"));

        BooksDTO booksDTO = new BooksDTO(books);
        return new MessageResponse(HttpStatus.OK.toString(), "Book retrieved successfully!", booksDTO);
    }

    @CacheEvict(value = "booksCache", key = "'allBooks'")
    @Transactional
    public MessageResponse addBook(BooksDTO booksDTO) {
        if(booksRepository.existsByIsbn(booksDTO.getIsbn()))
            throw new CustomException("A book with the same ISBN already exists");

        Books books = modelMapper.map(booksDTO, Books.class);
        booksRepository.save(books);

        return new MessageResponse(HttpStatus.CREATED.toString(), "Book added successfully!", books);
    }

    @CacheEvict(value = "booksCache", key = "#id")
    @Transactional
    public MessageResponse updateBook(Long id, BooksDTO booksDTO) {
        Optional<Books> existingBookOptional = booksRepository.findById(id);
        if(existingBookOptional.isEmpty())
            throw new CustomException("Book not found!");

        Books existingBooks = existingBookOptional.get();
        existingBooks.setIsbn(booksDTO.getIsbn());
        existingBooks.setTitle(booksDTO.getTitle());
        existingBooks.setAuthor(booksDTO.getAuthor());
        booksRepository.save(existingBooks);

        return new MessageResponse(HttpStatus.OK.toString(), "Book updated successfully!", existingBooks);
    }

    @CacheEvict(value = "booksCache", key = "#id")
    @Transactional
    public MessageResponse deleteBook(Long id) {
        Optional<Books> existingBookOptional = booksRepository.findById(id);
        if(existingBookOptional.isEmpty())
            throw new CustomException("Book not found!");

        Books existingBooks = existingBookOptional.get();
        booksRepository.delete(existingBooks);

        return new MessageResponse(HttpStatus.OK.toString(), "Book deleted successfully!", null);
    }
}
