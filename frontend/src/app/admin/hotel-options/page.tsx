"use client";

import Navigation from "@/components/navigation/Navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  addHotelOption,
  getAllHotelOptions,
  modifyHotelOption,
} from "@/lib/api/Admin/HotelOptionApi";
import { OptionResponse } from "@/lib/types/admin/response/OptionResponse";
import { useEffect, useState } from "react";

export default function HotelOptionsPage() {
  const [options, setOptions] = useState<OptionResponse[]>([]);
  const [editingOptionId, setEditingOptionId] = useState<number | null>(null);
  const [editingValues, setEditingValues] = useState<Record<number, string>>(
    {}
  );
  const [newOption, setNewOption] = useState<string>("");

  useEffect(() => {
    fetchOptions();
  }, []);

  const fetchOptions = async () => {
    try {
      const data = await getAllHotelOptions();
      console.log("옵션 데이터 확인:", data);
      setOptions(data);
    } catch (error) {
      console.error("호텔 옵션을 불러오는 중 오류 발생:", error);
    }
  };

  const onModify = async (optionId: number) => {
    if (!editingValues[optionId] || !editingValues[optionId].trim()) return;

    try {
      await modifyHotelOption(optionId, { name: editingValues[optionId] });
      setEditingOptionId(null);
      setEditingValues((prev) => {
        const newValues = { ...prev };
        delete newValues[optionId];
        return newValues;
      });
      fetchOptions();
    } catch (error) {
      console.error("옵션 수정 중 오류 발생:", error);
    }
  };

  const onAddOption = async () => {
    if (!newOption.trim()) return;
    try {
      await addHotelOption({ name: newOption });
      setNewOption("");
      fetchOptions();
    } catch (error) {
      console.error("옵션 추가 중 오류 발생:", error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navigation /> {/* 네비게이션 추가 */}
      {/* 컨테이너 */}
      <div className="max-w-3xl mx-auto pt-24 p-6">
        <h1 className="text-2xl font-bold mb-6 text-center">
          🏨 호텔 옵션 관리
        </h1>

        {/* 옵션 추가 박스 */}
        <div className="bg-white p-4 rounded-lg shadow-md flex gap-2">
          <Input
            type="text"
            placeholder="새 옵션 입력"
            value={newOption}
            onChange={(e) => setNewOption(e.target.value)}
          />
          <Button onClick={onAddOption}>추가</Button>
        </div>

        {/* 옵션 목록 */}
        <div className="mt-6 space-y-4">
          {options.map((option) => (
            <div
              key={option.optionId}
              className="bg-white p-4 rounded-lg shadow-md flex justify-between items-center"
            >
              {editingOptionId === option.optionId ? (
                <Input
                  value={editingValues[option.optionId] ?? ""}
                  onChange={(e) =>
                    setEditingValues((prev) => ({
                      ...prev,
                      [option.optionId]: e.target.value,
                    }))
                  }
                />
              ) : (
                <span className="text-gray-700">{option.name}</span>
              )}

              {editingOptionId === option.optionId ? (
                <Button
                  onClick={() => onModify(option.optionId)}
                  variant="default"
                >
                  저장
                </Button>
              ) : (
                <Button
                  onClick={() => {
                    setEditingOptionId(option.optionId);
                    setEditingValues((prev) => ({
                      ...prev,
                      [option.optionId]: option.name,
                    }));
                  }}
                  variant="secondary"
                >
                  수정
                </Button>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
