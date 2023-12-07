import React from 'react';
import './label.css';
import { ToastContainer, toast } from 'react-toastify';

function Label(props) {

    function handleClick() {
        if (props.info) {
            console.log(props.info);
            toast.info(props.info, {
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                pauseOnFocusLoss: true,
                draggable: true,
                progress: undefined,
                theme: "dark"
            });
        }
    };

    const value = props.value;
    const icon = props.icon?.toLowerCase();
    const labelClass = `label ${props.variant ? props.variant : ""}`;

    return (
        <div className={labelClass}>
            <button className={labelClass} onClick={handleClick}>
                {icon ? <img src={`./svg/labels/${icon}.svg`} alt={value} /> : <strong>{value}</strong>}
            </button>
            <ToastContainer limit={10} />
        </div>
    );
};

export default Label;
