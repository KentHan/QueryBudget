package com.odde.securetoken;

import java.time.LocalDate;
import java.util.List;

public class QueryBudget {

    private BudgetDatasource budgetDatasource;

    public QueryBudget(BudgetDatasource budgetDatasource) {
        this.budgetDatasource = budgetDatasource;
    }

    public Double query(LocalDate start, LocalDate end) {

        if(start.isAfter(end)) {
            throw new RuntimeException("start date > end date");
        }

        List<Budget> budgets = budgetDatasource.findAllBudgets();

        double sum = 0;

        for (int i = 0; i < budgets.size(); i++) {

            Budget budget = budgets.get(i);

            String[] date = budget.getMonth().split("/");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);

            if (isBudgetIncluded(start, end, year, month)) {

                int daysOfMonth = LocalDate.of(year, month, 1).lengthOfMonth();

                if (start.getMonthValue() == month && start.getYear() == year) {
                    int startCoverDays =
                            (start.getMonthValue() == end.getMonthValue() && start.getYear() == end.getYear()) ?
                            end.getDayOfMonth() - start.getDayOfMonth() + 1
                                                                                                               :
                            daysOfMonth - start.getDayOfMonth() + 1;
                    sum += budget.getMoney() / daysOfMonth * startCoverDays;
                    continue;

                }
                if (end.getMonthValue() == month && end.getYear() == year) {
                    int endCoverDays = end.getDayOfMonth();
                    sum += budget.getMoney() / daysOfMonth * endCoverDays;
                    continue;
                }

                sum += budget.getMoney();

            }
        }

        return sum;
    }

    private boolean isBudgetIncluded(LocalDate start, LocalDate end, int budgetYear, int budgetMonth) {

        if (start.getYear() == end.getYear()){
            return budgetMonth >= start.getMonthValue() && budgetMonth <= end.getMonthValue();
        } else {
            return budgetYear >= start.getYear() && budgetYear <= end.getYear();
        }

    }

}
