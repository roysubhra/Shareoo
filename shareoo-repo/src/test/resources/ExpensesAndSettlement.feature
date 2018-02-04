Feature: Expenses and Settlements

  Background:
    Given Following Users Exist
      | firstName | lastName | emailId        | phoneNumber | regNumber |
      | User 1    | User 1   | user1@test.com | 1234567891  | 1         |
      | User 2    | User 2   | user2@test.com | 1234567892  | 2         |
      | User 3    | User 3   | user3@test.com | 1234567893  | 3         |
    And Following Share Groups Exist
      | shareGroupId | createdBy | name          | members | expenses | liabilities | settlements | active |
      | S0           | 1         | Picnic        | [3,1]   | []       | []          | []          | false  |
      | S1           | 1         | Movie On 13th | [1,2]   | []       | []          | []          | true   |
      | S2           | 2         | Picnic        | [2,3]   | []       | []          | []          | true   |

  Scenario Outline: An Expense is added
    When User calls POST on path:users/<regNumber>/sharegroups/<shareGroupId>/expenses with <Expense>
    Then Response status code is <code>
    And Response is <json>
    And Liabilities for path:users/<regNumber>/sharegroups/<shareGroupId> equals <liabilities>
    Examples:
      | shareGroupId | regNumber | code | liabilities              | json                                                                                                                                                                                                                               |
      | S1           | 1         | 200  | {"1":{},"2":{"1":60.00}} | [{"receiptNo":"1234","receiptDate":1517687556177,"createdBy":"1","description":"Movie Tickets","amount":120.00}]                                                                                                                   |
      | S1           | 2         | 200  | {"1":{"2":90.00},"2":{}} | [{"receiptNo":"1234","receiptDate":1517687556177,"createdBy":"1","description":"Movie Tickets","amount":120.00},{"receiptNo":"12345","receiptDate":1517687556177,"createdBy":"2","description":"Movie Tickets 2","amount":300.00}] |

  Scenario Outline: A member is added to a Group having expenses
    Given ShareGroup is changed
      | {"shareGroupId":"S1","createdBy":"1","name":"Movie On 13th","members":["1","2"],"expenses":[{"receiptNo":"1234","receiptDate":1517687556177,"createdBy":"1","description":"Movie Tickets","amount":120.00},{"receiptNo":"12345","receiptDate":1517687556177,"createdBy":"2","description":"Movie Tickets 2","amount":300.00}],"liabilities":{"1":{"2":90.00},"2":{}},"settlements":[],"active":true} |
    When User calls POST without Data on path:users/<regNumber>/sharegroups/<shareGroupId>/members/<newMember>
    Then Response status code is <code>
    And Response is <json>
    And Liabilities for path:users/<regNumber>/sharegroups/<shareGroupId> equals <liabilities>
    Examples:
      | newMember | shareGroupId | regNumber | code | liabilities              | json                                                                                                                                                                                                                                                                                                                                                 |
      | 3         | S1           | 1         | 200  | {"1":{},"2":{"1":60.00}} | [{"regNumber":"1","firstName":"User 1","lastName":"User 1","emailId":"user1@test.com","phoneNumber":"1234567891"},{"regNumber":"2","firstName":"User 2","lastName":"User 2","emailId":"user2@test.com","phoneNumber":"1234567892"},{"regNumber":"3","firstName":"User 3","lastName":"User 3","emailId":"user3@test.com","phoneNumber":"1234567893"}] |


  Scenario Outline: A member makes a Settlement
    Given ShareGroup is changed
      | {"shareGroupId":"S1","createdBy":"1","name":"Movie On 13th","members":["1","2","3"],"expenses":[{"receiptNo":"1234","receiptDate":1517687556177,"createdBy":"1","description":"Movie Tickets","amount":120.00},{"receiptNo":"12345","receiptDate":1517687556177,"createdBy":"2","description":"Movie Tickets 2","amount":300.00}],"liabilities":{"1":{"2":20.00},"2":{},"3":{"2":140.00}},"settlements":[],"active":true} |
    When User calls POST on path:users/<regNumber>/sharegroups/<shareGroupId>/settlements with <settlement>
    Then Response status code is <code>
    And Response is <json>
    And Liabilities for path:users/<regNumber>/sharegroups/<shareGroupId> equals <liabilities>
    Examples:
      | settlement                                                                           | shareGroupId | regNumber | code | liabilities                      | json                                                                                           |
      | {{"settlementId":"STL1", "paidTo": "2", "paymentDate":1517687556177,"amount":20.00}} | S1           | 1         | 200  | {"1":{},"2":{},"3":{"2":140.00}} | [{"settlementId":"STL1","paidBy":"1","paidTo":"2","paymentDate":1517687556177,"amount":20.00}] |