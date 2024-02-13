Feature: Item
  Scenario Outline: For add item to table.
    Given I have filled all input boxes with valid data
    When I click on the add item button
    Then I should see the item in the table


    Examples:
      | name | description | price | quantity |
      | item1 | description1 | 10 | 1 |
      | item2 | description2 | 20 | 2 |
      | item3 | description3 | 30 | 3 |



  Scenario: Fetch all data
    Given find all list
    And find item by id
