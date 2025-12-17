Feature: Search Books

@InputSearch
Scenario Outline: Search Books
When the user navigates to the catalogue of books
And user types “<word>” in the search input
Then the books with title “<title>” are displayed

Examples:
  | word            | expected_title                                                                      |
  | git             | Git Pocket Guide                                                                    |
  | GIT             | Git Pocket Guide                                                                    |
  | design          | Designing Evolvable Web APIs with ASP.NET, Learning JavaScript Design Patterns      |
