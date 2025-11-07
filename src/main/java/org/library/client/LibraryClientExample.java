package org.library.client;

import org.library.generated.client.api.DefaultApi;
import org.library.generated.client.invoker.ApiClient;
import org.library.generated.client.model.BookDto;

import java.util.List;

public class LibraryClientExample {
    public static void main(String[] args) {
        ApiClient client = new ApiClient();
        client.setBasePath("http://localhost:8080/api/v1");

        DefaultApi api = new DefaultApi(client);

        List<BookDto> books = api.getBooksByReaderId(2L);
        System.out.println("The most popular book is: " + books);
    }
}
