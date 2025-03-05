export const logout = async () => {
  try {
    const response = await fetch("http://localhost:8080/api/users/logout", {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (response.status === 204) {
      return;
    }

    const rsData = await response.json();
    if (!response.ok) {
      throw new Error(rsData.msg);
    }

    return rsData;
  } catch (error) {
    throw error;
  }
};

export const join = async (joinRequest: {
  email: string;
  name: string;
  phoneNumber: string;
  role: string;
  provider: string;
  oauthId: string;
  birthDate: string;
}) => {
  const response = await fetch("http://localhost:8080/api/users/join", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(joinRequest),
  });

  return await response.json();
};
