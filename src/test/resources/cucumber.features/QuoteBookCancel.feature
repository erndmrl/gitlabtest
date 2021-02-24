@browser
Feature: Quote, Book, Cancel

  @wip
  Scenario: Single Pickup Only With No Time Intervals

    * Take an authorization token and request to Quote for Single ASAP Pickup
    * Quote Response: Http response status code should be 201
    * Quote Response: Check response parameters
    * Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
    * Verify the quoted job is listed on the UI
    * Verify the status of the quoted job is "Quoted" on the UI
    * Request to book
    * Book Response: Http response status code should be 201
    * Book Response: Check response parameters
    * Verify the status of the quoted job is "Booked" on the UI
    * Request to cancel the job
    * Cancel Booking Response: Http response status code should be 200
    * Verify the status of the quoted job is "Cancelled" on the UI



  Scenario: Single Pickup Only With Pickup Time Intervals Only Within the Same Day

    * Take an authorization token and request to Quote for Single Scheduled Same Day Pickup
    * Quote Response: Http response status code should be 201
    * Quote Response: Check response parameters
    * Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
    * Verify the quoted job is listed on the UI
    * Verify the status of the quoted job is "Quoted" on the UI
    * Request to book
    * Book Response: Http response status code should be 201
    * Book Response: Check response parameters
    * Verify the status of the quoted job is "Booked" on the UI
    * Request to cancel the job
    * Cancel Booking Response: Http response status code should be 200
    * Verify the status of the quoted job is "Cancelled" on the UI



  Scenario: Single Pickup Only With Pickup Time Intervals Only on Any Other Day

    * Take an authorization token and request to Quote for Single Scheduled Any Other Day Pickup
    * Quote Response: Http response status code should be 201
    * Quote Response: Check response parameters
    * Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
    * Verify the quoted job is listed on the UI
    * Verify the status of the quoted job is "Quoted" on the UI
    * Request to book
    * Book Response: Http response status code should be 201
    * Book Response: Check response parameters
    * Verify the status of the quoted job is "Booked" on the UI
    * Request to cancel the job
    * Cancel Booking Response: Http response status code should be 200
    * Verify the status of the quoted job is "Cancelled" on the UI



  Scenario: Single Pickup and Delivery With Both Pickup and Delivery Time Intervals Within the Same Day

    * Take an authorization token and request to Quote for Single Scheduled Same Day Delivery
    * Quote Response: Http response status code should be 201
    * Quote Response: Check response parameters
    * Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
    * Verify the quoted job is listed on the UI
    * Verify the status of the quoted job is "Quoted" on the UI
    * Request to book
    * Book Response: Http response status code should be 201
    * Book Response: Check response parameters
    * Verify the status of the quoted job is "Booked" on the UI
    * Request to cancel the job
    * Cancel Booking Response: Http response status code should be 200
    * Verify the status of the quoted job is "Cancelled" on the UI



  Scenario: Single Pickup and Delivery With Both Pickup and Delivery Time Intervals on Any Other Day

    * Take an authorization token and request to Quote for Single Scheduled Any Other Day Delivery
    * Quote Response: Http response status code should be 201
    * Quote Response: Check response parameters
    * Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
    * Verify the quoted job is listed on the UI
    * Verify the status of the quoted job is "Quoted" on the UI
    * Request to book
    * Book Response: Http response status code should be 201
    * Book Response: Check response parameters
    * Verify the status of the quoted job is "Booked" on the UI
    * Request to cancel the job
    * Cancel Booking Response: Http response status code should be 200
    * Verify the status of the quoted job is "Cancelled" on the UI



  Scenario: Single Pickup and Delivery With Only Delivery Time Intervals Within the Same Day

    * Take an authorization token and request to Quote for Single Scheduled Delivery Windows - Same Day
    * Quote Response: Http response status code should be 201
    * Quote Response: Check response parameters
    * Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
    * Verify the quoted job is listed on the UI
    * Verify the status of the quoted job is "Quoted" on the UI
    * Request to book
    * Book Response: Http response status code should be 201
    * Book Response: Check response parameters
    * Verify the status of the quoted job is "Booked" on the UI
    * Request to cancel the job
    * Cancel Booking Response: Http response status code should be 200
    * Verify the status of the quoted job is "Cancelled" on the UI



  Scenario: Single Pickup and Delivery With Only Delivery Time Intervals on Any Other Day

    * Take an authorization token and request to Quote for Single Scheduled Delivery Windows - Any Other Day
    * Quote Response: Http response status code should be 201
    * Quote Response: Check response parameters
    * Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
    * Verify the quoted job is listed on the UI
    * Verify the status of the quoted job is "Quoted" on the UI
    * Request to book
    * Book Response: Http response status code should be 201
    * Book Response: Check response parameters
    * Verify the status of the quoted job is "Booked" on the UI
    * Request to cancel the job
    * Cancel Booking Response: Http response status code should be 200
    * Verify the status of the quoted job is "Cancelled" on the UI



  Scenario: Single Drop - Immediate Booking, Quote & Book - ASAP

    * Take an authorization token and request to Book for Single Drop - Immediate Booking, Quote & Book - ASAP
    * Book Response: Http response status code should be 201
    * Book Response: Check response parameters
    * Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
      * Verify the quoted job is listed on the UI
    * Verify the status of the quoted job is "Booked" on the UI
    * Request to cancel the job
    * Cancel Booking Response: Http response status code should be 200
    * Verify the status of the quoted job is "Cancelled" on the UI



  Scenario: Single Drop - Immediate Booking, Quote & Book - Scheduled today

    * Take an authorization token and request to Book for Single Drop - Immediate Booking, Quote & Book - Scheduled today
    * Book Response: Http response status code should be 201
    * Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
    * Verify the quoted job is listed on the UI
    * Verify the status of the quoted job is "Booked" on the UI
    * Request to cancel the job
    * Cancel Booking Response: Http response status code should be 200
    * Verify the status of the quoted job is "Cancelled" on the UI



  Scenario: Single Drop - Immediate Booking, Quote & Book - Scheduled Any Other Day

    * Take an authorization token and request to Book for Single Drop - Immediate Booking, Quote & Book - Scheduled Any Other Day
    * Book Response: Http response status code should be 201
    * Log in to the UI with following credentials
      | username         | password |
      | sysadmin@lineten | test     |
    * Verify the quoted job is listed on the UI
    * Verify the status of the quoted job is "Booked" on the UI
    * Request to cancel the job
    * Cancel Booking Response: Http response status code should be 200
    * Verify the status of the quoted job is "Cancelled" on the UI
