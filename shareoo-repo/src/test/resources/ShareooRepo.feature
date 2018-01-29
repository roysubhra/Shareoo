Feature: Find User
  Scenario: Successful Find for User by Email
    When: User with Email test@test.com Exists
      And: Client calls /users/test@test.com
    Then: Clients receives User Data where emailId is test@test.com
      And: Response status code is 200


