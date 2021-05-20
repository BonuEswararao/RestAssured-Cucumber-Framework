package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
public class StepDefinition extends Utils{
	RequestSpecification req;
	ResponseSpecification resspec;
	TestDataBuild data = new TestDataBuild();
	Response response;
	static String place_id;
	
	@Given("Add Place Payload with {string} {string} {string}")
	public void add_Place_Payload_with(String name, String language, String address) throws IOException {
		req = given().spec(requestSpecification()).body(data.addPlacePayLoad(name, language, address));

	}

	@When("user calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String httpMethod) {
	    APIResources resourceAPI = APIResources.valueOf(resource);
	    resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
	    if(httpMethod.equalsIgnoreCase("POST")) {
	    	response = req.when().post(resourceAPI.getResource()).then().spec(resspec).extract().response();
	    }else if(httpMethod.equalsIgnoreCase("GET")) {
	    	response = req.when().get(resourceAPI.getResource()).then().spec(resspec).extract().response();
	    }
	    
	}

	@Then("the API call got success with status code {int}")
	public void the_API_call_got_success_with_status_code(Integer statusCode) {
	    // Write code here that turns the phrase above into concrete actions
		assertEquals(response.getStatusCode(),200);
	}

	@Then("{string} in response body is {string}")
	public void in_response_body_is(String keyValue, String expectedValue) {
		System.out.println("key value is :"+getJsonPath(response,keyValue));
		assertEquals(expectedValue,getJsonPath(response,keyValue));
		
	}
	
	@Then("verify placeId created maps to {string} using {string}")
	public void verify_placeId_created_maps_to_using(String name, String resource) throws IOException {
		place_id = getJsonPath(response,"place_id");
		req = given().spec(requestSpecification()).queryParam("place_id", place_id);
		user_calls_with_http_request(resource,"GET");
		String actualName = getJsonPath(response,"name");
		assertEquals(name,actualName);
	}
	
	@Given("deletePlace Payload")
	public void deleteplace_Payload() throws IOException {
		req = given().spec(requestSpecification()).body(data.deletePlacePayload(place_id));
	}

}
