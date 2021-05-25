import {Skill} from "./skill";

export class MemberSkills {

  skills: Skill[];
  mainSkill: string;

  constructor(skills: Skill[], mainSkill: string) {
    this.skills = skills;
    this.mainSkill = mainSkill;
  }
}
