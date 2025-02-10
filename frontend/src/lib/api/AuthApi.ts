export const logout = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/users/logout', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    const rsData = await response.json();
    if (rsData.resultCode !== '200-1') {
      throw new Error(rsData.msg);
    }

    return rsData;
  } catch (error) {
    throw error;
  }
}; 