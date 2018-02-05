Feature: Share Group

  Background:
    Given Following Users Exist
      | firstName | lastName | emailId        | phoneNumber | regNumber |
      | User 1    | User 1   | user1@test.com | 1234567891  | 1         |
      | User 2    | User 2   | user2@test.com | 1234567892  | 2         |
      | User 3    | User 3   | user3@test.com | 1234567893  | 3         |
    And Following Share Groups Exist
      | {"shareGroupId":"S1","createdBy":"1","name":"Movie On 13th","members":["1","2"],"expenses":[],"liabilities":{},"settlements":[],"active":true} |
      | {"shareGroupId":"S2","createdBy":"2","name":"Picnic","members":["2"],"expenses":[],"liabilities":{},"settlements":[],"active":true}            |

  Scenario Outline:Find Share Groups for User
    When User calls GET on path:users/<regNumber>/sharegroups
    Then Response status code is <code>
    And Response is <json>
    Examples:
      | regNumber | code | json                                                                                                                                                                                                                                                                                             |
      | 1         | 200  | [{"shareGroupId":"S1","createdBy":"1","name":"Movie On 13th","members":["1","2"],"expenses":[],"liabilities":{},"settlements":[],"active":true}]                                                                                                                                                 |
      | 2         | 200  | [{"shareGroupId":"S1","createdBy":"1","name":"Movie On 13th","members":["1","2"],"expenses":[],"liabilities":{},"settlements":[],"active":true},{"shareGroupId":"S2","createdBy":"2","name":"Picnic","members":["2"],"expenses":[],"liabilities":{},"settlements":[],"active":true}] |
      | 3         | 200  | []                                                                                                                                                                                                                                                                                               |
      | 4         | 404  |  |

  Scenario Outline:Add a Share Group
    When User calls POST on path:users/<regNumber>/sharegroups with <postBody>
    Then Response status code is <code>
    And Response is <json>
    Examples:
      | regNumber | code | postBody                                    | json                                                                                                                                      |
      | 1         | 200  | {"shareGroupId":"S3","name":"Andaman Trip"} | {"shareGroupId":"S3","createdBy":"1","name":"Andaman Trip","members":["1"],"expenses":[],"liabilities":{},"settlements":[],"active":true} |
      | 4         | 404  | {"shareGroupId":"S4","name":"Andaman Trip"} |  |

  Scenario Outline:List Members for a Share Group
    When User calls GET on path:users/<regNumber>/sharegroups/<shareGroupId>/members
    Then Response status code is <code>
    And Response is <json>
    Examples:
      | shareGroupId | regNumber | code | json                                                                                                                                                                                                                                |
      | S1           | 1         | 200  | [{"regNumber":"1","firstName":"User 1","lastName":"User 1","emailId":"user1@test.com","phoneNumber":"1234567891"},{"regNumber":"2","firstName":"User 2","lastName":"User 2","emailId":"user2@test.com","phoneNumber":"1234567892"}] |
      | S2           | 2         | 200  | [{"regNumber":"2","firstName":"User 2","lastName":"User 2","emailId":"user2@test.com","phoneNumber":"1234567892"}]                                                                                                                  |
      | S0           | 3         | 404  |  |
      | S1           | 3         | 404  |  |
      | S4           | 4         | 404  |  |

  Scenario Outline: Add Member to a Share Group
    When User calls POST without Data on path:users/<regNumber>/sharegroups/<shareGroupId>/members/<newMember>
    Then Response status code is <code>
    And Response is <json>
    Examples:
      | shareGroupId | regNumber | newMember | code | json                                                                                                                                                                                                                                                                                                                                                 |
      | S1           | 1         | 3         | 200  | [{"regNumber":"1","firstName":"User 1","lastName":"User 1","emailId":"user1@test.com","phoneNumber":"1234567891"},{"regNumber":"2","firstName":"User 2","lastName":"User 2","emailId":"user2@test.com","phoneNumber":"1234567892"},{"regNumber":"3","firstName":"User 3","lastName":"User 3","emailId":"user3@test.com","phoneNumber":"1234567893"}] |
      | S2           | 2         | 1         | 200  | [{"regNumber":"1","firstName":"User 1","lastName":"User 1","emailId":"user1@test.com","phoneNumber":"1234567891"},{"regNumber":"2","firstName":"User 2","lastName":"User 2","emailId":"user2@test.com","phoneNumber":"1234567892"}]                                                                                                                  |
      | $4           | 2         | 3         | 404  |  |
      | $4           | 4         | 3         | 404  |  |
