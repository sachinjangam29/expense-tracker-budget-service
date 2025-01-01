// BudgetManagementDashboard.jsx
import React, {useState} from 'react';
import {WalletIcon, CalendarIcon, PlusIcon} from 'lucide-react';
import "/styles/BudgetManagementDashboard.css";
import {BudgetEntryState} from "../../types/BudgetEntryState";
import {BudgetEntryErrorState} from "../../types/BudgetEntryErrorState";

interface BudgetEntryProps {
    formData: BudgetEntryState;
    formDataError: BudgetEntryErrorState;
    handleInputChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    handleSubmit: (e: React.FormEvent) => void;
}

const BudgetManagementDashboard: React.FC<BudgetEntryProps> = ({
                                                                   formData,
                                                                   formDataError,
                                                                   handleInputChange,
                                                                   handleSubmit
                                                               }) => {
    const [budgets, setBudgets] = useState([
        {
            budget_id: 1,
            start_date: new Date(2024, 0, 1),
            end_date: new Date(2024, 11, 31),
            allocated_amount: 500000.00,
            carry_forward: 10000
        }
    ]);
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [newBudget, setNewBudget] = useState({
        start_date: '',
        end_date: '',
        allocated_amount: '',
        carry_forward: ''
    });

    const handleAddBudget = () => {
        const budget = {
            budget_id: budgets.length + 1,
            start_date: new Date(newBudget.start_date),
            end_date: new Date(newBudget.end_date),
            allocated_amount: parseFloat(newBudget.allocated_amount),
            carry_forward: parseFloat(newBudget.carry_forward)
        };
        setBudgets([...budgets, budget]);
        setIsDialogOpen(false);
        setNewBudget({
            start_date: '',
            end_date: '',
            allocated_amount: '',
            carry_forward: ''
        });
    };

    const formatDate = (date) => {
        return new Date(date).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    };

    const formatCurrency = (amount) => {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(amount);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="container">
                <h1>Expense Tracker</h1>

                <div className="setup-form">
                    <h2>Track Period</h2>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Start Date</label>
                            <input type="date"
                                   id="startDate"
                                   required
                                   value={formData.startDate ? formData.startDate.toISOString().split('T')[0] : ""}
                                   onChange={handleInputChange}
                            />
                            {formDataError.startDate && typeof formDataError.startDate === 'string' &&
                                <div className="error">{formDataError.startDate}</div>}
                        </div>
                        <div className="form-group">
                            <label>End Date</label>
                            <input type="date"
                                   id="endDate"
                                   required
                                   value={formData.endDate ? formData.endDate.toISOString().split('T')[0] : ""}
                                   onChange={handleInputChange}/>
                        </div>
                    </div>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Allotment Money</label>
                            <input type="number"
                                   id="allocatedAmount"
                                   placeholder="Enter amount"
                                   required
                                   value={formData.allocatedAmount}
                                   onChange={handleInputChange}
                            />
                        </div>
                        <div className="form-group">
                            <label>Carry Forward</label>
                            <input type="number"

                                   id="carryForward"
                                   required
                                   value={formData.carryForward}
                                   placeholder="Enter amount"
                                   onChange={handleInputChange}
                            />
                        </div>
                    </div>
                    <button>Set Tracking Period</button>
                </div>

                <div className="summary">
                    <div className="card allotment">
                        <h3>Total Budget</h3>
                        <div className="amount">$5,000.00</div>
                    </div>
                    <div className="card carry-forward">
                        <h3>Remaining Balance</h3>
                        <div className="amount">$3,580.00</div>
                    </div>
                </div>

                <div className="expenses-list">
                    <h3>Expense Records</h3>
                    <div className="expense-item">
                        <div className="expense-info">
                            <div>Groceries</div>
                            <div className="expense-date">Apr 1, 2024</div>
                        </div>
                        <div className="expense-amount">-$120.00</div>
                    </div>
                    <div className="expense-item">
                        <div className="expense-info">
                            <div>Utilities</div>
                            <div className="expense-date">Apr 2, 2024</div>
                        </div>
                        <div className="expense-amount">-$200.00</div>
                    </div>
                </div>
            </div>
        </form>
    );
};

export default BudgetManagementDashboard;