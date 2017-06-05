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
public class ModelYearTest {
    Constants constants = Constants.instance();
    private Gson gson;
    private JsonParser gsonPaser;

    public ModelYearTest() throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        gsonPaser = new JsonParser();
    }

    @DataProvider(name = "localeListFromExcelWB")
    public Object[][] localeListFromExcelWBTest() {
        return Utils.getLocaleListFromExcelWB();
    }

    @Test(dataProvider = "localeListFromExcelWB", groups = {"modelYear", "car"})
    public void getModelYearsTests(String locale) throws IOException {
        System.out.println("Cars Available!");
        System.out.println("-----------------------------------------------------");
        BrandTest brandTest = new BrandTest();
        ModelTest modelTest = new ModelTest();
        ModelYearTest modelYearTest = new ModelYearTest();

        // 1. User is able to pick up a 'Car Brand'
        JsonObject brandList = brandTest.getBrands(locale);

        String manufacturerCode;
        String mainTypeCode;
        JsonElement brandCar;
        JsonElement modelCar;
        JsonElement yearCar;

        Set<Map.Entry<String, JsonElement>> brands = brandList.entrySet();
        for (Map.Entry<String, JsonElement> brand : brands) {
            manufacturerCode = brand.getKey();
            brandCar = brand.getValue();

            // 2. User is able to pick up a 'Car Model'
            JsonObject modelList = modelTest.getModels(locale, manufacturerCode);

            Set<Map.Entry<String, JsonElement>> models = modelList.entrySet();
            for (Map.Entry<String, JsonElement> model : models) {
                mainTypeCode = model.getKey();
                modelCar = model.getValue();

                // 3. User is able to pick up a 'Vehicle Model Year'
                JsonObject modelYearList = modelYearTest.getModelYears(locale, manufacturerCode, mainTypeCode);

                Set<Map.Entry<String, JsonElement>> modelYears = modelYearList.entrySet();
                for (Map.Entry<String, JsonElement> year : modelYears) {
                    yearCar = year.getValue();
                    System.out.println("{locale:"+locale+", brand:"+brandCar+", model: "+modelCar+", year: "+yearCar+"}");
                }
            }
            System.out.println("\n");
        }
    }

    public JsonObject getModelYears(String locale, String manufacturer, String model) {
        Object response;
        response =
                given().
                        queryParameters(constants.requestParam1, locale).
                        queryParameters(constants.requestParam2, manufacturer).
                        queryParameters(constants.requestParam3, model).
                        queryParameters(constants.requestAuth, constants.requestAuthValue).
                when().
                        get(constants.baseHost + constants.service3BasePath).
                then().
                        assertThat().
                        statusCode(200).
                        contentType(JSON).
                        extract().path("wkda");
        String yearsJsonObj = gson.toJson(response);
        return (JsonObject) gsonPaser.parse(new StringReader(yearsJsonObj));
    }
}
