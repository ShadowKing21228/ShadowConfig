package net.shadowking21.shadowconfig.config.models;

import net.shadowking21.shadowconfig.annotation.ConfigComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomConfig
{
    @ConfigComment("Just a random commentary for random value \nAnd... next string of comment")
    public String randomValue = "random";

    @SuppressWarnings("unused")
    @ConfigComment("Just a random commentary for random value TWO")
    public String randomValueTwo = "randomTwo";

    @SuppressWarnings("unused")
    public List<Integer> randomIntValues = new ArrayList<>(List.of(5, 4, 2, 3, 1));

    @SuppressWarnings("unused")
    public Map<String, Double> randomMapValues = new HashMap<>(Map.of("value1", 1.111, "value2", 2.222));

    @SuppressWarnings("unused")
    public boolean trueValue = true;

    @SuppressWarnings("unused")
    public boolean falseValue = false;

    @ConfigComment("Finally it ready to release!")
    @SuppressWarnings("unused")
    public String additionalValue = "HAHAHA I DO IT!";

    @ConfigComment("Nested Config start")
    public NestedConfig config = new NestedConfig();

    public RandomConfig() {}
}