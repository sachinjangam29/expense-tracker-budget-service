import React, {FormEvent, useState} from "react";
import BudgetManagementDashboard from "./BudgetManagementDashboard";
import {BudgetEntryState} from "../../types/BudgetEntryState";
import {BudgetEntryErrorState} from "../../types/BudgetEntryErrorState";
import {validateForm} from "../../utils/validations/BudgetCreationValidations.ts";

const BudgetManagementHome: React.FC = () => {

    // hardcoded username
    const username = "sachin";

    const [formData, setFormData] = useState<BudgetEntryState>({
        startDate: new Date(),
        endDate: new Date(),
        allocatedAmount: 0,
        carryForward: 0,
        username: ''
    });

    const [formDataError, setFormDataError] = useState<BudgetEntryErrorState>({
        startDate: null,
        endDate: null,
        allocatedAmount: null,
        carryForward: null,
    });

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { id, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [id]: id === "startDate" || id === "endDate" ? new Date(value) : Number(value),
        }));
    };

    const handleSubmit = async (e:FormEvent) =>{
        e.preventDefault();

        const errors = validateForm(formData);

        if (Object.values(errors).every((error) => !error)) {
            console.log(formData);

            formData.username = username;

            try{
                const response = await fetch("http://localhost:8082/api/v1/budget/create",
                    {
                        method: 'POST',
                        headers: {
                            'Content-Type':'application/json'
                        },
                        body: JSON.stringify(formData)
                    });
         //       const data = await response.json();
                const responseText = await response.text();

                if(!response.ok){
                //    throw new Error('Http error! status :$(response.status)')
                    console.error(`Http error! status: $(response.status)`)

                    try{
                        const errorData = JSON.parse(responseText);
                        console.error("Error Details: ", errorData);

                        alert(`Error: ${errorData.message || "Unknown error occured"}`);
                    }catch (parseError) {
                        console.error("Error parsing response:", parseError);
                        alert(`Error: ${responseText || "No details provided"}`);
                    }
                    return;
                }

                const data = JSON.parse(responseText);
                console.log("Response from server:", data);

                alert("Budget created successfully!");
                console.log("Response from server:", data);

                // Example: access specific fields

            } catch(error){
                console.error("error during budget entry", error)

                alert("Failed to create the budget");
            }
        }else{
            console.log("form has errors", errors);
        }
    }

    return (
        // <div className="container">
        //     <h1>Expense Tracker</h1>
        //
        //     <div className="setup-form">
        //         <h2>Track Period</h2>
        //         <div className="form-row">
                    <BudgetManagementDashboard formData={formData} formDataError={formDataError} handleInputChange={handleInputChange} handleSubmit={handleSubmit}/>
        //         </div>
        //     </div>
        // </div>
    );
};

export default BudgetManagementHome;