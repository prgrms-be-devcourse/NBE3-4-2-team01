"use client";

import {
  addHotelOption,
  deleteHotelOption,
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
  ); // 개별 수정 값 저장
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
        delete newValues[optionId]; // 수정 후 해당 옵션의 상태 삭제
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
    <div>
      <h1>호텔 옵션 관리</h1>

      {/* 옵션 추가 기능 */}
      <div>
        <input
          type="text"
          placeholder="새 옵션 입력"
          value={newOption}
          onChange={(e) => setNewOption(e.target.value)}
        />
        <button onClick={onAddOption}>추가</button>
      </div>

      {/* 옵션 목록 */}
      <div>
        {options.map((option) => (
          <div key={option.optionId}>
            {editingOptionId === option.optionId ? (
              <input
                value={editingValues[option.optionId] ?? ""}
                onChange={(e) =>
                  setEditingValues((prev) => ({
                    ...prev,
                    [option.optionId]: e.target.value,
                  }))
                }
              />
            ) : (
              <span>{option.name}</span>
            )}
            {editingOptionId === option.optionId ? (
              <button onClick={() => onModify(option.optionId)}>저장</button>
            ) : (
              <button
                onClick={() => {
                  setEditingOptionId(option.optionId);
                  setEditingValues((prev) => ({
                    ...prev,
                    [option.optionId]: option.name,
                  }));
                }}
              >
                수정
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
