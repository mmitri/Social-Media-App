import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';
//import {render, screen} from '@testing-library/react'
import userEvent from '@testing-library/user-event'


test('renders landing page', () => {
  render(<App />);
  //expect(linkElement).toBeInTheDocument();
});

test("renders components of home page", () => {
  render(<App/>);
  expect(screen.getByRole("heading")).toHaveTextContent(/WELCOME TO THE BUZZ/);
  expect(screen.getByRole("button", {name: "Add A Post"})).toBeDefined;

});

//new test

test('renders landing page', () => {
  render(<App/>);
  expect(screen.getByRole("heading")).toHaveTextContent(/Profile/);
});

//try to do mock fetching and testing
// beforeEach(() =>{
//   jest.spyOn(window, "fetch").mockImplementation();
// });

// afterEach(() =>{
//   jest.restoreAllMocks();
// })


test('click', () => {
  render(<App/>)

  userEvent.click(screen.getByRole("button", {name: "Add A Post"}))
  expect(screen.getByRole("button", {name: "Add"})).toBeDefined();
})

/*
Manual Tests include testing
the buttons route
the add/cancel buttons clear on click
liking and unliking
loading from api
making post with new post
*/




