import { ClientType } from '@quarano-frontend/health-department/domain';

export interface ReportCaseDto {
  dateOfBirth: Date;
  firstName: string;
  lastName: string;
  zipCode?: string;
  email: string;
  phone: string;
  enrollmentCompleted: boolean;
  caseType: ClientType;
  medicalStaff?: boolean;
  quarantineEnd: Date;
  quarantineStart: Date;
  status: string;
  caseId: string;
  caseTypeLabel: string;
}


