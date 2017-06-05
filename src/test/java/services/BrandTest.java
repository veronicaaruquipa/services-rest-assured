package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import support.Constants;
import support.Utils;

import java.io.IOException;
import java.io.StringReader;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;

/**
 * @project services-rest-assured
 * @user veronica.aruquipa
 * @date 04-06-17 08:09 PM
 */
public class BrandTest {
    Constants constants = Constants.instance();
    private Gson gson;
    private JsonParser gsonPaser;

    public BrandTest() throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        gsonPaser = new JsonParser();
    }

    @DataProvider(name = "localeListFromExcelWB")
    public Object[][] localeListFromExcelWBTest() {
        return Utils.getLocaleListFromExcelWB();
    }

    @Test(dataProvider = "localeListFromExcelWB", groups = {"brand", "car"})
    public void getBrandsTest(String locale) throws IOException {
        JsonObject brands = getBrands(locale);
        Assert.assertNotNull(brands);
    }

    public JsonObject getBrands(String locale){
        Object response;
        response =
                given().
                        queryParameters(constants.requestParam1, locale).
                        queryParameters(constants.requestAuth, constants.requestAuthValue).
                when().
                        get(constants.baseHost + constants.service1BasePath).
                then().
                        assertThat().
                        statusCode(200).
                        contentType(JSON).
                        extract().path("wkda");

        String brandsJsonObj = gson.toJson(response);
        return (JsonObject) gsonPaser.parse(new StringReader(brandsJsonObj));
    }
}
