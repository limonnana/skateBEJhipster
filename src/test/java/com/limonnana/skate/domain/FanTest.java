package com.limonnana.skate.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.limonnana.skate.web.rest.TestUtil;

public class FanTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fan.class);
        Fan fan1 = new Fan();
        fan1.setId("id1");
        Fan fan2 = new Fan();
        fan2.setId(fan1.getId());
        assertThat(fan1).isEqualTo(fan2);
        fan2.setId("id2");
        assertThat(fan1).isNotEqualTo(fan2);
        fan1.setId(null);
        assertThat(fan1).isNotEqualTo(fan2);
    }
}
