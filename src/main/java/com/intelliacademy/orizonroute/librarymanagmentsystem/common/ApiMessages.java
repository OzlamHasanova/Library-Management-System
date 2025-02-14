package com.intelliacademy.orizonroute.librarymanagmentsystem.common;


public class ApiMessages {

    public static class Book {
        public static final String GET_BOOKS_BY_CATEGORY = "Retrieve a list of books based on the specified category ID.";
        public static final String GET_BOOK_BY_ID = "Retrieve details of a specific book by its ID.";
    }

    public static class Author {
        public static final String GET_ALL_AUTHORS = "Retrieve a paginated list of all authors.";
    }

    public static class Category {
        public static final String GET_ALL_CATEGORIES = "Retrieve a paginated list of all categories.";
    }
}
