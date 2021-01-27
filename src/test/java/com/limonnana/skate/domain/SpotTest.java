package com.limonnana.skate.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.limonnana.skate.web.rest.TestUtil;

public class SpotTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Spot.class);
        Spot spot1 = new Spot();
        spot1.setId("id1");
        Spot spot2 = new Spot();
        spot2.setId(spot1.getId());
        assertThat(spot1).isEqualTo(spot2);
        spot2.setId("id2");
        assertThat(spot1).isNotEqualTo(spot2);
        spot1.setId(null);
        assertThat(spot1).isNotEqualTo(spot2);
    }
}
