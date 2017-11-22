package com.theastrologist.domain;

import org.junit.Test;
import com.theastrologist.util.CalcUtil;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class DegreeTest {

    @Test
    public void testNormalDegree() {
        Degree degree = new Degree(48, 39);
        assertThat(degree.getDegree(), equalTo(48));
        assertThat(degree.getMinutes(), equalTo(39));
        assertThat(degree.getSeconds(), equalTo(0.));
        assertThat(degree.getBaseDegree(), closeTo(48.65, CalcUtil.DELTA));
        assertThat(degree.toString(), equalTo("48° 39'"));
    }

    @Test
    public void testBaseDegree() {
        Degree degree = new Degree(48.6456630);
        assertThat(degree.getDegree(), equalTo(48));
        assertThat(degree.getMinutes(), equalTo(38));
        assertThat(degree.getSeconds(), closeTo(44.38685, CalcUtil.DELTA));
    }

    @Test
    public void testOtherBaseDegree() {
        Degree degree = new Degree(341.);
        assertThat(degree.getDegree(), equalTo(341));
        assertThat(degree.getMinutes(), equalTo(0));
        //assertThat(degree.getSeconds(), closeTo(0, CalcUtil.DELTA));
    }

    @Test
    public void testParseStringDegree() {
        Degree degree = new Degree("24°32'");
        assertThat(degree.getDegree(), equalTo(24));
        assertThat(degree.getMinutes(), equalTo(32));
        //assertThat(degree.getSeconds(), closeTo(0, CalcUtil.DELTA));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMalformed() {
        Degree degree = new Degree("2456f");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseToBigMinutes() {
        Degree degree = new Degree("456°75'");
    }
}