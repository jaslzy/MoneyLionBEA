package com.monlio.featureswitch.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest 
@AutoConfigureMockMvc 
public class FeatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //@BeforeEach // Jasper - this runs "before each row of @CsvSource {instead of, only once for entire @CsvSource}" or "for all @tests method {instead of, for specific @tests method that i would like}"
    public void setupDatabase() {
        jdbcTemplate.execute("DELETE FROM moneylion.feature");
    }

    private int countRowsInFeatureTable() {
        Integer output = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM moneylion.feature", Integer.class);

        if (output == null){return -1;}
        
        return output;
    }

    
    @Transactional
    @Rollback
    @Test
    public void testPostAndGetFeature() throws Exception{
        Object[][] csvData = {
            {true, 0, "example@gmail.com", "exampleFeature"},
            {false, 1, "trolo@gmail.com", "exampleFeature"},
            {false, 2, "qwer@gmail.com", "feature"},
            {false, 3, "qwe@gmail.com", "feature"},
            {false, 4, "jkw@gmail.com", "feature"}
        };

        for (Object[] rowData : csvData) {
            boolean hasYetToRunOnce = (boolean) rowData[0];
            int nRowInDB = (int) rowData[1];
            String email = (String) rowData[2];
            String featureName = (String) rowData[3];

            if (hasYetToRunOnce){
                setupDatabase();
            }
    
            assertEquals(nRowInDB, countRowsInFeatureTable());
    
            String strExpected_Initial = "{ \"canAccess\": " + false + " }";
             mockMvc.perform(MockMvcRequestBuilders.get("/feature")
             .param("email", email)
             .param("featureName", featureName))
             .andExpect(MockMvcResultMatchers.content().string(strExpected_Initial));
    
            String strContent = "{\"useremail\": \"" + email + "\", \"featurename\": \"" + featureName + "\", \"canAccess\": " + true + "}";
            mockMvc.perform(MockMvcRequestBuilders.post("/feature")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(strContent))
                    .andExpect(MockMvcResultMatchers.status().is(200));
    
             String strExpected_Final = "{ \"canAccess\": " + true + " }";
             mockMvc.perform(MockMvcRequestBuilders.get("/feature")
             .param("email", email)
             .param("featureName", featureName))
             .andExpect(MockMvcResultMatchers.content().string(strExpected_Final));
        }
        
    }

    @ParameterizedTest
    @Transactional
    @Rollback
    @CsvSource({
        "example@gmail.com,exampleFeature,true",
        "trolo@gmail.com,exampleFeature,false",
        "qwer@gmail.com,feature,true"
    })
    public void testGetFeature(String email, String featurename, boolean canAccess) throws Exception{
            setupDatabase();
            String addTwoRows = "INSERT INTO moneylion.feature (user_email, feature_name, can_access) " + 
                                "VALUES " +
                                "('example@gmail.com', 'exampleFeature', true), " +
                                "('qwer@gmail.com', 'feature', true);";
            jdbcTemplate.execute(addTwoRows);

        String strExpected = "{ \"canAccess\": " + canAccess + " }";
        
        mockMvc.perform(MockMvcRequestBuilders.get("/feature")
                .param("email", email)
                .param("featureName", featurename))
                .andExpect(MockMvcResultMatchers.content().string(strExpected));
    }

    @ParameterizedTest
    @Transactional
    @Rollback 
    @CsvSource({
            "example@gmail.com, exampleFeature, false, 200",
            "example@gmail.com, exampleFeature, true, 200",
            "qwer@gmail.com, feature, true, 200",
            
            ", exampleFeature, true, 304", // email is null
            "'', exampleFeature, true, 304", // email is empty string
            "@gmail.com, exampleFeature, true, 304",
            "example, exampleFeature, true, 304",
            "example@gmailcom, exampleFeature, true, 304",
            "example@gmailcom., exampleFeature, true, 304",
            "C8SAb1CSoKsojAiQTYqBMtRXWIZLQ3s7gUfogTihcNIUlgc5MsHtQASTOnPWlBZ0FW1CYCm2x9ktqDZni47GO9kWtrWnUlHoTpgVHNFHoSjkn90AHFfDl5B6j0Yq4GWc3eq0TapKwW0WP0NocB9Igf4ZtKYShCPlgN6XGLjg0cFJmd4oN6sUWTVKDqe6AxjNPpuXuBho4gVQ766yQlVY1K0aSkuFjVGPDHDj16ZvK3nZ707RnzOnSC@gmail.com, exampleFeature, true, 304", //this email as 256 char, whereas mysql only allow 255 char
            
            "example@gmail.com, , true, 304", // featurename is null
            "example@gmail.com, '', true, 304", // featurename is empty string
            "example@gmail.com, '   ', true, 304", //featurename only contains white space char
            "example@gmail.com, C8SAb1CSoKsojAiQTYqBMtRXWIZLQ3s7gUfogTihcNIUlgc5MsHtQASTOnPWlBZ0FW1CYCm2x9ktqDZni47GO9kWtrWnUlHoTpgVHNFHoSjkn90AHFfDl5B6j0Yq4GWc3eq0TapKwW0WP0NocB9Igf4ZtKYShCPlgN6XGLjg0cFJmd4oN6sUWTVKDqe6AxjNPpuXuBho4gVQ766yQlVY1K0aSkuFjVGPDHDj16ZvK3nZ707RnzOnSCovljoacTox, true, 304", //this feature has 256 char, whereas mysql only allow 255 char

            "example@gmail.com, exampleFeature, , 304" // canAccess is null
    })
    public void testPostFeature(String email, String featureName, Boolean canAccess, int expectedStatus) throws Exception {
        String strContent = "";

        if (email == null){strContent = "{\"featurename\": \"" + featureName + "\", \"canAccess\": " + canAccess + "}";}
        else if (featureName == null){strContent = "{\"useremail\": \"" + email + "\", \"canAccess\": " + canAccess + "}";}
        else if (canAccess == null) {strContent = "{\"useremail\": \"" + email + "\", \"featurename\": \"" + featureName + "\"}";}
        else {strContent = "{\"useremail\": \"" + email + "\", \"featurename\": \"" + featureName + "\", \"canAccess\": " + canAccess + "}";}

        mockMvc.perform(MockMvcRequestBuilders.post("/feature")
                .contentType(MediaType.APPLICATION_JSON)
                .content(strContent))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus));
               
    }

}
