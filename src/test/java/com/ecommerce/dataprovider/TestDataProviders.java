package com.ecommerce.dataprovider;

import org.testng.annotations.DataProvider;

public class TestDataProviders {

    @DataProvider(name = "invalidLoginData")
    public static Object[][] getInvalidLoginData() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out."},
                {"standard_user", "invalid_password", "Epic sadface: Username and password do not match any user in this service"},
                {"", "secret_sauce", "Epic sadface: Username is required"},
                {"standard_user", "", "Epic sadface: Password is required"}
        };
    }

    @DataProvider(name = "productSortingData")
    public static Object[][] getProductSortingData() {
        return new Object[][]{
                {"Name (A to Z)"},
                {"Name (Z to A)"},
                {"Price (low to high)"},
                {"Price (high to low)"}
        };
    }

    @DataProvider(name = "searchKeywords")
    public static Object[][] getSearchKeywords() {
        return new Object[][]{
                {"dress"},
                {"tshirt"},
                {"jeans"}
        };
    }
}
