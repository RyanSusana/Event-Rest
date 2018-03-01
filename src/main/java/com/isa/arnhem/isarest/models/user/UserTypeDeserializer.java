package com.isa.arnhem.isarest.models.user;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Optional;

public class UserTypeDeserializer extends JsonDeserializer<UserType> {
    @Override
    public UserType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Optional<UserType> rank = UserType.getRank(jsonParser.getValueAsString());

        if (rank.isPresent()) {
            return rank.get();
        } else {
            throw new IllegalArgumentException(jsonParser.getValueAsString() + " is not a valid rank");
        }
    }
}
