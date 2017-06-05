package services;

import com.google.gson.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import support.Constants;
import support.Utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;

/**
 * @project services-rest-assured
 * @user veronica.aruquipa
 * @date 04-06-17 08:09 PM
 */
public class ModelTest {
    Constants constants = Constants.instance();
    private Gson gson;
    private JsonParser gsonPaser;

    public ModelTest() throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        gsonPaser = new JsonParser();
    }

    @DataProvider(name = "localeListFromExcelWB")
    public Object[][] localeListFromExcelWBTest() {
        return Utils.getLocaleListFromExcelWB();
    }

    @Test(dataProvider = "localeListFromExcelWB", groups = {"model", "car"})
    public void getModelsTests (String locale) throws IOException {
        BrandTest brandTest = new BrandTest();
        ModelTest modelTest = new ModelTest();

        // 1. User is able to pick up a 'Car Brand'
        JsonObject brandList = brandTest.getBrands(locale);

        String manufacturerCode;

        Set<Map.Entry<String, JsonElement>> brands = brandList.entrySet();
        for (Map.Entry<String, JsonElement> brand : brands) {
            manufacturerCode = brand.getKey();

            // 2. User is able to pick up a 'Car Model'
            JsonObject modelList = modelTest.getModels(locale, manufacturerCode);
            Assert.assertNotNull(modelList);

        }
    }

    public JsonObject getModels(String locale, String manufacturer) {
        Object response;
        response =
                given().
                        queryParameters(constants.requestParam1, locale).
                        queryParameters(constants.requestParam2, manufacturer).
                        queryParameters(constants.requestAuth, constants.requestAuthValue).
                        when().
                        get(constants.baseHost + constants.service2BasePath).
                        then().
                        assertThat().
                        statusCode(200).
                        contentType(JSON).
                        extract().path("wkda");

        String modelsJsonObj = gson.toJson(response);
        return (JsonObject) gsonPaser.parse(new StringReader(modelsJsonObj));
    }
}
