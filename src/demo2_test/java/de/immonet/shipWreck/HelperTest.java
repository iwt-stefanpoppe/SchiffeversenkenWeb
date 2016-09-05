package de.immonet.shipWreck;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Created by spoppe on 01.09.2016.
 */
public class HelperTest {
    @Test
    public void inputEvaluatorReturns77UnexpectedNumberInInput() throws Exception {
        Assert.assertEquals(Helper.inputEvaluator("A9"),new Integer(77));
    }

    @Test
    public void inputEvaluatorReturns77UnexpectedLetterInInput() throws Exception {

        Assert.assertEquals(Helper.inputEvaluator("Z3"),new Integer(77));
    }

    @Test
    public void inputEvaluatorReturns77CompletelyUnexpectedInput() throws Exception {
        Assert.assertEquals(Helper.inputEvaluator("X9"),new Integer(77));
    }

    @Test
    public void inputEvaluatorReturns77InputIsToLong() throws Exception{
        Assert.assertEquals(Helper.inputEvaluator("F37"),new Integer(77));
    }

    @Test
    public void inputEvaluatorReturns77NoNumberOnSecondPlaceOfInput() throws Exception{
        Assert.assertEquals(Helper.inputEvaluator("A-3"),new Integer(77));
    }

    @Test
    public void inputEvaluatorReturns0LowerBorder() throws Exception{
        Assert.assertEquals(Helper.inputEvaluator("A0"),new Integer(0));
    }

    @Test
    public void inputEvaluatorReturns48UpperBorder() throws Exception{
        Assert.assertEquals(Helper.inputEvaluator("G6"),new Integer(48));
    }

    @Test
    public void inputEvaluatorReturns24SomethingInBetween() throws Exception{
        Assert.assertEquals(Helper.inputEvaluator("D3"),new Integer(24));
    }

    @Ignore
    @Test
    public void thisTestHasToFail() throws Exception{
        Assert.assertEquals(Helper.inputEvaluator("A1"),new Integer (3));
    }


}