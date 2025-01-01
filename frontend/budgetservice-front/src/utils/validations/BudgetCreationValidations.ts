import {BudgetEntryState} from "../../types/BudgetEntryState.tsx";
import {BudgetEntryErrorState} from "../../types/BudgetEntryErrorState.tsx";

const validateField = (fieldName: string, value: any): string | null => {
    switch (fieldName) {
        case "startDate":
        case "endDate":
            if (!value) return "This field is required.";
            if (!(value instanceof Date) || isNaN(value.getTime())) {
                return "Invalid date.";
            }
            return null;

        case "allocatedAmount":
        case "carryForward":
            if (typeof value !== 'number' || value < 0) {
                return "Value must be a positive number.";
            }
            return null;

        default:
            return null;
    }
};


export const validateForm = (formData: BudgetEntryState): BudgetEntryErrorState => {
    return {
        startDate: validateField("startDate", formData.startDate),
        endDate: validateField("endDate", formData.endDate),
        allocatedAmount: validateField("allocatedAmount", formData.allocatedAmount),
        carryForward: validateField("carryForward", formData.carryForward)
    };
};