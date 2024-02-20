import { render, screen } from '@testing-library/react';
import Home from './index';

test('renders Home page title', () => {
  render(<Home />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
