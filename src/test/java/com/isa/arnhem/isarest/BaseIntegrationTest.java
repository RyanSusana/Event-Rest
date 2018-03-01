package com.isa.arnhem.isarest;

import com.github.fakemongo.Fongo;
import com.isa.arnhem.isarest.repository.serialization.CustomObjectIdUpdater;
import lombok.Getter;
import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;
import org.junit.Before;

@Getter
public abstract class BaseIntegrationTest {

    private Jongo jongo;

    @Before
    public void init() {
        Fongo fongo = new Fongo("Fongo Test Server");
        jongo = new Jongo(fongo.getDB("isa-rest"), new JacksonMapper.Builder()
                .withObjectIdUpdater(new CustomObjectIdUpdater())
                .build());
    }


}
