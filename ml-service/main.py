from typing import List
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()


class MatchingItem(BaseModel):
    id: int
    type: str
    skillsRequired: List[str] = []
    experienceRequired: int = 0
    title: str


class MatchRequest(BaseModel):
    skills: List[str] = []
    experience: int = 0
    items: List[MatchingItem]


class MatchResult(BaseModel):
    id: int
    matchPercent: int


class MatchResponse(BaseModel):
    results: List[MatchResult]


def normalize(skills: List[str]) -> List[str]:
    return [skill.strip().lower() for skill in skills]


def calculate_match(candidate_skills, candidate_experience, required_skills, required_experience):
    if not required_skills:
        return 0
    candidate = normalize(candidate_skills)
    required = normalize(required_skills)
    overlap = sum(1 for skill in required if skill in candidate)
    skill_score = overlap / len(required)
    experience_score = 1.0 if required_experience == 0 else min(candidate_experience / required_experience, 1.0)
    percent = round((skill_score * 0.7 + experience_score * 0.3) * 100)
    return max(0, min(100, percent))


@app.post("/match", response_model=MatchResponse)
async def match(request: MatchRequest):
    results = []
    for item in request.items:
        score = calculate_match(request.skills, request.experience, item.skillsRequired, item.experienceRequired)
        results.append(MatchResult(id=item.id, matchPercent=score))
    return MatchResponse(results=results)
