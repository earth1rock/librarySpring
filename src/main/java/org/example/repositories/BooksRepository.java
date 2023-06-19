package org.example.repositories;

import org.example.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer>
{
	boolean existsByTitleAndAuthor(String title, String author);
}
