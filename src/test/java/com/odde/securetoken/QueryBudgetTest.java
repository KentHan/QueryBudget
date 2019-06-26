package com.odde.securetoken;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class QueryBudgetTest {

    private LocalDate start;
    private LocalDate end;
    private BudgetDatasource budgetDatasource = mock(BudgetDatasource.class);

    @Test
    public void testGetWhole9Month(){

        giveBudget(
                new Budget("2018/10", 31),
                new Budget("2018/9", 30)

        );

        givenStartDate(2018, 9, 1);
        givenEndDate(2018,9,30);

        QueryBudget queryBudget = new QueryBudget(budgetDatasource);
        Double result = queryBudget.query(start, end);

        assertEquals((Double)30.0, result);

    }

    @Test
    public void testGetWhole10Month(){

        giveBudget(
                new Budget("2018/10", 31),
                new Budget("2018/11", 30)

        );

        givenStartDate(2018, 10, 1);
        givenEndDate(2018,10,31);

        QueryBudget queryBudget = new QueryBudget(budgetDatasource);
        Double result = queryBudget.query(start, end);

        assertEquals((Double)31.0, result);

    }

    @Test
    public void testGetTwoWholeMonths(){

        giveBudget(
                new Budget("2018/10", 31),
                new Budget("2018/11", 30)

        );

        givenStartDate(2018, 10, 1);
        givenEndDate(2018,11,30);

        QueryBudget queryBudget = new QueryBudget(budgetDatasource);
        Double result = queryBudget.query(start, end);

        assertEquals((Double)61.0, result);

    }

    @Test
    public void testGetOneDay(){

        giveBudget(
                new Budget("2018/10", 31)
        );

        givenStartDate(2018, 10, 1);
        givenEndDate(2018,10,1);

        QueryBudget queryBudget = new QueryBudget(budgetDatasource);
        Double result = queryBudget.query(start, end);

        assertEquals((Double)1.0, result);

    }

    @Test
    public void testCrossMonth(){

        giveBudget(
                new Budget("2018/10", 31),
                new Budget("2018/11", 30),
                new Budget("2018/12", 31)
        );

        givenStartDate(2018, 10, 31);
        givenEndDate(2018,12,1);

        QueryBudget queryBudget = new QueryBudget(budgetDatasource);
        Double result = queryBudget.query(start, end);

        assertEquals((Double)32.0, result);

    }

    @Test
    public void testCrossYear(){

        giveBudget(
                new Budget("2018/12", 31),
                new Budget("2019/1", 31)
        );

        givenStartDate(2018, 12, 31);
        givenEndDate(2019,1,2);

        QueryBudget queryBudget = new QueryBudget(budgetDatasource);
        Double result = queryBudget.query(start, end);

        assertEquals((Double)3.0, result);

    }

    @Test
    public void testCrossYearWithEmptyMonth(){

        giveBudget(
                new Budget("2018/12", 31),
                new Budget("2019/2", 28)
        );

        givenStartDate(2018, 12, 31);
        givenEndDate(2019,2,2);

        QueryBudget queryBudget = new QueryBudget(budgetDatasource);
        Double result = queryBudget.query(start, end);

        assertEquals((Double)3.0, result);

    }

    @Test
    public void testWithoutBudget(){

        givenStartDate(2018, 12, 29);
        givenEndDate(2018,12,30);

        QueryBudget queryBudget = new QueryBudget(budgetDatasource);
        Double result = queryBudget.query(start, end);

        assertEquals((Double)0.0, result);
    }

    @Test(expected = Exception.class)
    public void testException_End_Start(){

        givenStartDate(2018, 12, 31);
        givenEndDate(2018,12,30);

        QueryBudget queryBudget = new QueryBudget(budgetDatasource);
        Double result = queryBudget.query(start, end);


    }

    private void givenStartDate(int year, int month, int day) {
        start =  LocalDate.of(year, month, day);
    }

    private void givenEndDate(int year, int month, int day) {
        end = LocalDate.of(year, month, day);
    }


    private void giveBudget(Budget... budgets) {

        when(budgetDatasource.findAllBudgets()).thenReturn(Arrays.asList(budgets));

    }

}
