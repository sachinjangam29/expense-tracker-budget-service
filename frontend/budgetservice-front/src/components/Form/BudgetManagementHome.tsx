import React, {FormEvent, useState} from "react";
import BudgetManagementDashboard from "./BudgetManagementDashboard";
import {BudgetEntryState} from "../../types/BudgetEntryState";
import {BudgetEntryErrorState} from "../../types/BudgetEntryErrorState";
import {validateForm} from "../../utils/validations/BudgetCreationValidations.ts";

const BudgetManagementHome: React.FC = () => {
    const [formData, setFormData] = useState<BudgetEntryState>(
        {
            startDate: new Date(),
            endDate: new Date(),
            allotmentAmount: 0,
            carryForward: 0
        }
    );

    const [formDataError, setFormDataError] = useState<BudgetEntryErrorState>(
        {
            startDate: new Date(),
            endDate: new Date(),
            allotmentAmount: 0,
            carryForward: 0
        }
    );

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {id, value} = e.target;
        setFormData((prevData:BudgetEntryState) => ({
            ...prevData,
            [id]: id === "startDate" || id === "endDate" ? new Date(value) : value,
        }));
    };

    const handleSubmit = async (e:FormEvent) =>{
        e.preventDefault();

        const errors = validateForm(formData);

        if(Object.values(errors).every((error) => error === '')){
            console.log(formData);

            try{
                const response = await fetch("http://localhost:8082/api/v1/budget/create",
                    {
                        method: 'POST',
                        headers: {
                            'Content-Type':'application/json'
                        },
                        body: JSON.stringify(formData)
                    });
                if(!response.ok){
                    throw new Error('Http error! status :$(response.status)')
                }
                const data = await response.json();
                console.log("Budget created sucessfully");
                alert('Budget created sucessfully...');
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