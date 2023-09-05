/**
 * 
 */
package ita.app.boot.start;

import java.util.Scanner;

import ita.app.dto.Employee;
import ita.app.dto.MasterData;
import ita.app.service.CredentialService;
import ita.app.service.impl.CredentialServiceImpl;
import ita.app.utils.DataCollector;

/**
 * 
 */
public final class ITAssistApp implements Runnable {
	@Override
	public void run() {

		/**
		 * Creating instances for various classes to be used.
		 */
		Scanner scanner = new Scanner(System.in);
		DataCollector dataCollector = new DataCollector();
		String employeeFirstName = "";
		String employeeLastName = "";

		try {

			employee_credential_generation_form: while (true) {

				/**
				 * Step 1. Accept Employee First Name
				 */
				System.out.println("Please enter first name of the employee: ");
				employeeFirstName = scanner.nextLine().toUpperCase();

				/**
				 * Step 2. Accept Employee Last Name
				 */
				System.out.println("Please enter last name of the employee: ");
				employeeLastName = scanner.nextLine().toUpperCase();

				Employee employee = new Employee(employeeFirstName, employeeLastName);

				System.out.println("Please enter user department from the following list:-");

				/**
				 * Step 3. Accept Employee Department
				 */
				System.out.println("List of departments:-");
				displayDepartmentsMenuInfo(dataCollector);
				String departmentId = scanner.nextLine();
				department_choice: do  {
					switch (departmentId) {
					case "1":
					case "2":
					case "3":
					case "4":
						employee.setDepartment(Integer.valueOf(departmentId));
						break department_choice;
					default:
						System.out.println("Invalid choice entered, please enter between Id(1-4): ");
						departmentId = scanner.nextLine();
						continue department_choice;
					}
					
				}while(true);

				/**
				 * Step 4. Storing User Entered Data
				 */
				dataCollector.setEmployee(employee);

				/**
				 * Step 5. Generating user credentials via Service
				 */
				CredentialService service = new CredentialServiceImpl();
				service.generatePassword();
				service.generateEmailAddress(employee);

				/**
				 * Step 6. Displaying the final Output
				 */
				System.out.println("Dear " + employee.getFirstName() + ",");
				System.out.println("Your generated credentials are as follows:-");
				System.out.println(service.showCredentials());

				do {
					System.out.println("Would you like to generate credentials for another employee: ");
					System.out.println("('Y' for Yes OR 'N' for No)");
					continuation_choice: while (true) {
						String wantToContinue = scanner.nextLine().toUpperCase();
						switch (wantToContinue) {
						case "Y":
							continue employee_credential_generation_form;
						case "N":
							break employee_credential_generation_form;
						default:
							continue continuation_choice;
						}
					}
				} while (true);
			}

			System.out.println("Thank you for using ITAssit.");
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Exception in setting random special character while generating password.");
			System.out.println(ex.getMessage());
			System.out.println(ex.getStackTrace()[0] + " " + ex.toString());
		} catch (java.util.InputMismatchException ex) {
			System.out.println("Exception in matching the user input with the given choices.");
			System.out.println(ex.getMessage());
			System.out.println(ex.getStackTrace()[0] + " " + ex.toString());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.out.println(ex.getStackTrace()[0] + " " + ex.toString());
		} finally {
			if (null != scanner) {
				scanner.close();
			}
		}

	}

	private void displayDepartmentsMenuInfo(DataCollector dataCollector) {
		MasterData masterData = dataCollector.shareMasterData();
		for (int i = 0; i < masterData.getDepartments().length; i++) {
			System.out.println(masterData.getDepartments()[i].displayUIMenuInfo());
		}
	}
}
