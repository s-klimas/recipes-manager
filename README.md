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
![home-not-logged-in](https://github.com/s-klimas/RecipesManager/assets/72698285/a14413ac-1012-4381-a731-9a55497f99cc)

#### Logged-in
![home-logged-in](https://github.com/s-klimas/RecipesManager/assets/72698285/b5bd69e2-0ff2-4a20-91bd-bd3229193272)

```java
    @GetMapping("/login")
    public String loginForm() {};
```
Includes a login form and links to registration and password recovery.
![login](https://github.com/s-klimas/RecipesManager/assets/72698285/0076baae-f3ea-4eb8-8174-b2f92c92f299)

```java
    @GetMapping("/register")
    public String registrationForm(Model model) {};
```
Endpoint displaying a form for registration. (password validation is not working)
![register](https://github.com/s-klimas/RecipesManager/assets/72698285/620b8103-cdd6-4d8c-8d7e-ba6848d71231)

```java
    @GetMapping("/forget-password")
    public String forget() {};
```
The beginning of the flow to account recovery.
![forget-password](https://github.com/s-klimas/RecipesManager/assets/72698285/b3743a6c-8f8b-4599-95c7-3c729744d9aa)

```java
    @GetMapping("/recovery")
    public String recovery(@RequestParam String token, @RequestParam String email, Model model) {};
```
Continue the flow to recover the account and ultimately set a new password. Flow is secured by a token that allows you to change the password for your account for 24 hours.
![recovery](https://github.com/s-klimas/RecipesManager/assets/72698285/d28a827d-0f0d-43ea-b866-59ebb0888f09)

```java
    @GetMapping("/recipes/{id}")
    public String recipe(@PathVariable long id, Model model) {};
```
Endpoint displaying a specific recipe and allowing it to be edited or deleted.
![recipe](https://github.com/s-klimas/RecipesManager/assets/72698285/04f8579c-8ecb-402d-9ff7-e5b9e0d7def5)

```java
    @GetMapping("/add-recipe")
    public String addRecipeForm(Model model) {};
```
Flow to create new recipes and add them to your account.
![add-recipe](https://github.com/s-klimas/RecipesManager/assets/72698285/c29f0fd7-f99e-423e-bb49-5bf4dfc09943)

### The application also contains POST endpoints which enable it to function correctly.


```All sensitive data has been removed from the programme code.```
