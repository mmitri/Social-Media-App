//https://github.com/bocacode/react-image-upload/tree/main/src

import { useForm } from "react-hook-form";
import { type } from '@testing-library/user-event/dist/type';
import React, { useState, useEffect } from 'react';
import { Link } from "react-router-dom";

function NewFilePage() {
  const { register, handleSubmit } = useForm() 

  const onSubmit = (data: any) => {
    console.log(data)
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
        <input {...register} type="file" name="picture" />
        <Link to ="/">
          <button id="addButton" onClick={onSubmit}>Add</button>
          <button id="addCancel">Cancel</button>
        </Link>
      </form>
  );
}

export default NewFilePage;