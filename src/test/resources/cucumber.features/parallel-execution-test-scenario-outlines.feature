@parallel-test
Feature: Scenario Outlines feature file

  Scenario Outline: <scen_out_row_num>
    Given Step from '<scen_out_row_num>' in 'parallel-execution-test-scenario-outlines' feature file

    Examples:
      | scen_out_row_num       |
      | Scenario Outline Row 1 |
      | Scenario Outline Row 2 |