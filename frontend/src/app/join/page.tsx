'use client';
import { useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';

export default function JoinPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  
  const provider = decodeURIComponent(searchParams.get('provider') || '');
  const oauthId = searchParams.get('oauthId') || '';

  const [formData, setFormData] = useState({
    email: '',
    name: '',
    phoneNumber: '',
    provider,
    oauthId,
    role: 'USER',
    birthDate: ''
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    const response = await fetch('http://localhost:8080/api/users/join', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formData),
    });

    const data = await response.json();
    
    if (data.resultCode === "200") {
      router.push('/?message=회원가입이 완료되었습니다');
    } else if (data.resultCode === "400" && data.msg.includes("이미 가입된")) {
      router.push('/?message=기존 계정에 소셜 로그인이 연동되었습니다');
    } else {
      alert(data.msg);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="max-w-[460px] w-full space-y-8 p-10 bg-white rounded-xl shadow-lg">
        <div className="text-center">
          <h2 className="text-4xl font-bold text-gray-900">회원가입</h2>
          <p className="mt-3 text-base text-gray-600">
            추가 정보를 입력해주세요
          </p>
        </div>

        <form onSubmit={handleSubmit} className="mt-8 space-y-6">
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">
                이메일 <span className="text-red-500">*</span>
              </label>
              <input
                type="email"
                value={formData.email}
                onChange={(e) => {
                  const value = e.target.value;
                  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
                  const isValid = emailRegex.test(value);
                  
                  setFormData({ ...formData, email: value });
                  
                  const emailInput = e.target;
                  if (!isValid && value !== '') {
                    emailInput.setCustomValidity('올바른 이메일 형식이 아닙니다.');
                  } else {
                    emailInput.setCustomValidity('');
                  }
                }}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
                required
                pattern="[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}"
                placeholder="example@email.com"
              />
              <p className="mt-1 text-sm text-gray-500">
                예시: example@email.com
              </p>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">이름</label>
              <input
                type="text"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">전화번호</label>
              <input
                type="tel"
                value={formData.phoneNumber}
                onChange={(e) => {
                  const value = e.target.value;
                  const formattedNumber = value
                    .replace(/[^0-9]/g, '')
                    .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
                    .replace(/(\-{1,2})$/g, "");
                  
                  setFormData({ ...formData, phoneNumber: formattedNumber });
                }}
                pattern="01[0-9]-[0-9]{3,4}-[0-9]{4}"
                placeholder="010-0000-0000"
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
                required
              />
              <p className="mt-1 text-sm text-gray-500">
                예시: 010-1234-5678
              </p>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">회원 유형</label>
              <select
                value={formData.role}
                onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
              >
                <option value="USER">일반 사용자</option>
                {/* <option value="BUSINESS">사업자</option> */}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">
                생년월일 <span className="text-red-500">*</span>
              </label>
              <input
                type="date"
                value={formData.birthDate}
                onChange={(e) => setFormData({ ...formData, birthDate: e.target.value })}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
                required
                max={new Date().toISOString().split('T')[0]}
              />
            </div>
          </div>

          <button
            type="submit"
            className="w-full flex justify-center py-3 px-4 border border-transparent rounded-md shadow-sm text-base font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            가입 완료
          </button>
        </form>
      </div>
    </div>
  );
} 