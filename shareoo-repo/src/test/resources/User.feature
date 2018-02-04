Feature: User

  Background:
    Given Following Users Exist
      | firstName | lastName | emailId        | phoneNumber | regNumber |
      | User 1    | User 1   | user1@test.com | 1234567891  | 1         |
      | User 2    | User 2   | user2@test.com | 1234567892  | 2         |
      | User 3    | User 3   | user3@test.com | 1234567893  | 3         |

  Scenario Outline: Find User by Email
    When User calls GET on path:users/<emailId>
    Then Response status code is <code>
    And Response is <json>
    Examples:
      | emailId        | code | json                                                                                                                   |
      | user1@test.com | 200  | {"regNumber":"1","firstName":"User 1","lastName":"User 1","emailId":"user1@test.com","phoneNumber":"1234567891"}       |
      | user2@test.com | 200  | {"regNumber":"2","firstName":"User 2","lastName":"User 2","emailId":"user2@test.com","phoneNumber":"1234567892"}       |
      | user3@test.com | 200  | {"regNumber":"3","firstName":"User 3","lastName":"User 3","emailId":"user3@test.com","phoneNumber":"1234567893"}       |
      | user4@test.com | 404  | {"timestamp":1517683694936,"status":404,"error":"Not Found","message":"User Not Found","path":"/users/user4@test.com"} |

  Scenario Outline:Find for User by phone number
    When User calls GET on path:users/<phoneNumber>
    Then Response status code is <code>
    And Response is <json>
    Examples:
      | phoneNumber | code | json                                                                                                               |
      | 1234567891  | 200  | {"regNumber":"1","firstName":"User 1","lastName":"User 1","emailId":"user1@test.com","phoneNumber":"1234567891"}   |
      | 1234567892  | 200  | {"regNumber":"2","firstName":"User 2","lastName":"User 2","emailId":"user2@test.com","phoneNumber":"1234567892"}   |
      | 1234567893  | 200  | {"regNumber":"3","firstName":"User 3","lastName":"User 3","emailId":"user3@test.com","phoneNumber":"1234567893"}   |
      | 1234567894  | 404  | {"timestamp":1517683694936,"status":404,"error":"Not Found","message":"User Not Found","path":"/users/1234567894"} |

  Scenario Outline:Find for User by id
    When User calls GET on path:users/<regNumber>
    Then Response status code is <code>
    And Response is <json>
    Examples:
      | regNumber | code | json                                                                                                             |
      | 1         | 200  | {"regNumber":"1","firstName":"User 1","lastName":"User 1","emailId":"user1@test.com","phoneNumber":"1234567891"} |
      | 2         | 200  | {"regNumber":"2","firstName":"User 2","lastName":"User 2","emailId":"user2@test.com","phoneNumber":"1234567892"} |
      | 3         | 200  | {"regNumber":"3","firstName":"User 3","lastName":"User 3","emailId":"user3@test.com","phoneNumber":"1234567893"} |
      | 4         | 404  | {"timestamp":1517683694936,"status":404,"error":"Not Found","message":"User Not Found","path":"/users/4"}        |

