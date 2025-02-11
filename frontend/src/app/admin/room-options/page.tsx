"use client";

import Navigation from "@/components/navigation/Navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  addRoomOption,
  getAllRoomOptions,
  modifyRoomOption,
} from "@/lib/api/Admin/RoomOptionApi";
import { OptionResponse } from "@/lib/types/admin/response/OptionResponse";
import { useEffect, useState } from "react";

export default function RoomOptionsPage() {
  const [options, setOptions] = useState<OptionResponse[]>([]);
  const [editingOptions, setEditingOptions] = useState<Record<number, string>>(
    {}
  );
  const [newOption, setNewOption] = useState<string>("");

  useEffect(() => {
    fetchOptions();
  }, []);

  const fetchOptions = async () => {
    try {
      const data = await getAllRoomOptions();
      setOptions(data);
      setEditingOptions({});
    } catch (error) {
      console.error("객실 옵션을 불러오는 중 오류 발생:", error);
    }
  };

  const onModify = async (id: number) => {
    if (!editingOptions[id]?.trim()) return;

    try {
      await modifyRoomOption(id, { name: editingOptions[id] });

      setOptions((prevOptions) =>
        prevOptions.map((option) =>
          option.optionId === id
            ? { ...option, name: editingOptions[id] }
            : option
        )
      );

      setEditingOptions((prev) => {
        const updatedEditingOptions = { ...prev };
        delete updatedEditingOptions[id];
        return updatedEditingOptions;
      });
    } catch (error) {
      console.error("객실 옵션 수정 중 오류 발생:", error);
    }
  };

  const onAddOption = async () => {
    if (!newOption.trim()) return;

    try {
      await addRoomOption({ name: newOption });
      setNewOption("");
      fetchOptions();
    } catch (error) {
      console.error("객실 옵션 추가 중 오류 발생:", error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navigation /> {/* 네비게이션 추가 */}
      {/* 컨테이너 */}
      <div className="max-w-3xl mx-auto pt-24 p-6">
        <h1 className="text-2xl font-bold mb-6 text-center">객실 옵션 관리</h1>

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
          {options.map((option, index) => (
            <div
              key={option.optionId ?? `option-${index}`}
              className="bg-white p-4 rounded-lg shadow-md flex justify-between items-center"
            >
              {editingOptions[option.optionId] !== undefined ? (
                <Input
                  value={editingOptions[option.optionId]}
                  onChange={(e) =>
                    setEditingOptions({
                      ...editingOptions,
                      [option.optionId]: e.target.value,
                    })
                  }
                />
              ) : (
                <span className="text-gray-700">{option.name}</span>
              )}

              {editingOptions[option.optionId] !== undefined ? (
                <Button
                  onClick={() => onModify(option.optionId)}
                  variant="default"
                >
                  저장
                </Button>
              ) : (
                <Button
                  onClick={() =>
                    setEditingOptions({
                      ...editingOptions,
                      [option.optionId]: option.name,
                    })
                  }
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
