package com.limonnana.skate.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.limonnana.skate.web.rest.TestUtil;

public class TrickTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trick.class);
        Trick trick1 = new Trick();
        trick1.setId("id1");
        Trick trick2 = new Trick();
        trick2.setId(trick1.getId());
        assertThat(trick1).isEqualTo(trick2);
        trick2.setId("id2");
        assertThat(trick1).isNotEqualTo(trick2);
        trick1.setId(null);
        assertThat(trick1).isNotEqualTo(trick2);
    }
}
