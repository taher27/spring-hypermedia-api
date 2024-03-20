package com.baeldung.web.controller;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.baeldung.model.Book;
import com.baeldung.persistence.BookRepository;
import com.baeldung.web.resource.BookResource;
import com.baeldung.web.error.EntityNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerAddBookToCartTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private BookRepository bookRepo;

    private BookResource bookResource;
    private Book book;

    @Before
    public void setUp() {
        book = new Book();
        book.setIsbn("123456");

        bookResource = new BookResource(book);
    }

    @Test
    public void testAddValidBookToCart() {
        when(bookRepo.findByIsbn(book.getIsbn())).thenReturn(book);

        assertNotNull(cartController.addBookToCart(bookResource));
        assertTrue(cartController.seeYourCart().getBooks().contains(book));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testNonExistentBookAddToCart() {
        when(bookRepo.findByIsbn(book.getIsbn())).thenReturn(null);
        
        cartController.addBookToCart(bookResource);
    }

    @Test
    public void testMultipleAdditionSameBookToCart() {
        when(bookRepo.findByIsbn(book.getIsbn())).thenReturn(book);

        cartController.addBookToCart(bookResource);
        cartController.addBookToCart(bookResource);

        assertEquals(2, cartController.seeYourCart().getBooks().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyIsbnAddToCart() {
        book.setIsbn("");
        
        cartController.addBookToCart(bookResource);
    }
}
