Feature: Validating Place API's

  @AddPlace
  Scenario Outline: Verify if Place is being successfully added using AdddPlaceAPI
    Given Add Place Payload with "<name>" "<language>" "<address>"
    When user calls "addPlaceAPI" with "POST" http request
    Then the API call got success with status code 200
    And "status" in response body is "OK"
    And "scope" in response body is "APP"
    And verify placeId created maps to "<name>" using "getPlaceAPI"

    Examples: 
      | name    | language | address            |
      | AAhouse | english  | world cross center |

  # | BBhouse | spanish  | sea cross center   |
  @DeletePlace
  Scenario: Verify if the Delete Place functionality is working
    Given deletePlace Payload
    When user calls "deletePlaceAPI" with "POST" http request
    Then the API call got success with status code 200
    And "status" in response body is "OK"
