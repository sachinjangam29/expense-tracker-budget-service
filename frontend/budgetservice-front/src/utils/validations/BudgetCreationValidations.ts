const validateField = (fieldName: string, value: any): string | null => {
    switch (fieldName) {
        case "startDate":
        case "endDate":
            if (!value) return "This field is required.";
            if (!/^\d{2}-\d{2}-\d{4}$/.test(value)) return "Invalid date format. Use MM-DD-YYYY."; // Validate format
            if (isNaN(new Date(value).getTime())) return "Invalid date format. Use Use MM-DD-YYYY."; // Validate valid date
            return null;

        case "allotmentAmount":
        case "carryForward":
            if (isNaN(value) || parseFloat(value) <= 0) return "Value must be a positive number.";
            return null;

        default:
            return null;
    }
};

export const validateForm = (formData:
                             {
                                 startDate: Date,
                                 endDate: Date,
                                 allotmentAmount: number,
                                 carryForward: number
                             }) => {
    let errors = {
        startDate: validateField('startDate', formData.startDate),
        endDate: validateField('endDate', formData.endDate),
        allotmentAmount: validateField('allotmentAmount', formData.allotmentAmount),
        carryForward: validateField('carryForward', formData.carryForward),
    };
    return errors;
}