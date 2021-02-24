Feature: Parametrized Quote&Book

  Scenario: Change request parameters and request to quote and book

    * Take an authorization token, change request parameters as stated below using JsonPath then request to Quote
      | Action | Parameter to Remove or Value to Change | New Value |
      | Change | something                              | something |
      | Remove | .['Tasks'][1]['RequestedWindow']~      |           |


    * (P)Quote Response: Http response status code should be 201
    * (P)Quote Response: Check response parameters
    * (P)Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
    * (P)Verify the quoted job is listed on the UI
    * (P)Verify the status of the quoted job is "Quoted" on the UI
    * (P)Request to book
    * (P)Book Response: Http response status code should be 201
    * (P)Book Response: Check response parameters
    * (P)Request to cancel the job
    * (P)Cancel Booking Response: Http response status code should be 200

   
