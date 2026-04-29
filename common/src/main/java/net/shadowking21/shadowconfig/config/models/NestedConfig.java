package net.shadowking21.shadowconfig.config.models;

import net.shadowking21.shadowconfig.annotation.ConfigComment;

public class NestedConfig {

    @ConfigComment("This is a nested config value")
    public String nestedValue = "Nested String Value";

    @ConfigComment("This is a nested int config value")
    public int nestedintValue = 52;

    @ConfigComment("This is a nested boolean config value")
    public boolean nestedboolValue = true;
}
