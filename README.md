# Recipe Manager / RM

A web application to help you store your recipes.

## Technology used
### Backend
    - Java
    - Java Spring
    - Hibernate
    - Liquibase
    - Mailtrap
### Frontend
    - HTML
    - Bootstrap
    - CSS
    - JS
### Testing
    - JUnit 5
    - AssertJ
    - Mockito
    - Mockaroo (for generating test data)

## Endpoints available for the web browser
```java
    @GetMapping("/")
    public String home(Model model) {};
```
Endpoint has 2 containers. For the non-logged-in user, the application description page is displayed (Lorem ipsum...) and for the logged-in user, the regulatory overview view.
#### Non-logged-in
[img]
#### Logged-in
[img]

```java
    @GetMapping("/login")
    public String loginForm() {};
```
Includes a login form and links to registration and password recovery.
[img]

```java
    @GetMapping("/register")
    public String registrationForm(Model model) {};
```
Endpoint displaying a form for registration. (password validation is not working)
[img]

```java
    @GetMapping("/forget-password")
    public String forget() {};
```
The beginning of the flow to account recovery.
[img]

```java
    @GetMapping("/recovery")
    public String recovery(@RequestParam String token, @RequestParam String email, Model model) {};
```
Continue the flow to recover the account and ultimately set a new password. Flow is secured by a token that allows you to change the password for your account for 24 hours.
[img]

```java
    @GetMapping("/recipes/{id}")
    public String recipe(@PathVariable long id, Model model) {};
```
Endpoint displaying a specific recipe and allowing it to be edited or deleted.
[img]

```java
    @GetMapping("/add-recipe")
    public String addRecipeForm(Model model) {};
```
Flow to create new recipes and add them to your account.
[img]

### The application also contains POST endpoints which enable it to function correctly.


```All sensitive data has been removed from the programme code.```