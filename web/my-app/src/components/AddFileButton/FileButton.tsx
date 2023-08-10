
//https://github.com/bocacode/react-image-upload/tree/main/src

import { useForm } from "react-hook-form";
import { type } from '@testing-library/user-event/dist/type';
import React, { useState, useEffect } from 'react';
import "./FileButton.css"

function NewFilePage() {
  const { register, handleSubmit } = useForm() 

  const onSubmit = (data) => {
    console.log(data)
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input {...register} type="file" name="picture" />
      <button>Submit</button>
    </form>
  );
}

export default NewFilePage;